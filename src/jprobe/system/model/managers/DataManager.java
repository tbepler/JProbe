package jprobe.system.model.managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import util.ByteCounterOutputStream;
import util.ClassLoaderObjectInputStream;
import util.WorkerThread;
import util.osgi.OSGIUtils;
import util.save.ImportException;
import util.save.LoadException;
import util.save.SaveException;
import util.save.Saveable;
import util.save.SaveableEvent;
import util.save.SaveableListener;
import jprobe.services.data.Data;
import jprobe.services.Workspace;
import jprobe.services.WorkspaceEvent;
import jprobe.services.WorkspaceEvent.Type;
import jprobe.services.WorkspaceListener;

public class DataManager implements Saveable{
	
	private final ReadWriteLock m_WorkspaceListenersLock = new ReentrantReadWriteLock(true);
	private final ReadWriteLock m_SaveableListenersLock = new ReentrantReadWriteLock(true);
	private final ReadWriteLock m_ChangesLock = new ReentrantReadWriteLock(true);
	private final ReadWriteLock m_NameLock = new ReentrantReadWriteLock(true);
	private final ReadWriteLock m_DataLock = new ReentrantReadWriteLock(true);
	
	private final Collection<WorkspaceListener> m_WorkspaceListeners = new HashSet<WorkspaceListener>();
	private final Collection<SaveableListener> m_SaveableListeners = new HashSet<SaveableListener>();
	
	private final List<Data> m_Data = new ArrayList<Data>();
	private final Map<String, Data> m_NameToData = new HashMap<String, Data>();
	private final Map<Data, String> m_DataToName = new HashMap<Data, String>();
	
	private final BundleContext m_Context;
	private final Workspace m_Parent;

	private final WorkerThread m_EventThread;
	
	private boolean m_ChangesSinceLastSave = false;
	private String m_Name;
	
	public DataManager(BundleContext context, Workspace parent, String name, WorkerThread eventThread){
		m_Context = context;
		m_Parent = parent;
		m_Name = name;
		m_EventThread = eventThread;
	}
	
	public boolean isEmpty(){
		boolean result;
		m_DataLock.readLock().lock();
		try{
			result = m_Data.isEmpty();
		}finally{
			m_DataLock.readLock().unlock();
		}
		return result;
	}
	
	public void shutdownAndWait() throws InterruptedException{
		m_EventThread.shutdown();
		m_EventThread.waitForShutdown();
	}

	protected void notifyListeners(final WorkspaceEvent event){
		m_EventThread.execute(new Runnable(){

			@Override
			public void run() {
				m_WorkspaceListenersLock.readLock().lock();
				try{
					for(WorkspaceListener l : m_WorkspaceListeners){
						l.update(m_Parent, event);
					}
				}finally{
					m_WorkspaceListenersLock.readLock().unlock();
				}
			}

		});
	}
	
	protected void notifyListeners(final SaveableEvent event){
		m_EventThread.execute(new Runnable(){

			@Override
			public void run() {
				m_SaveableListenersLock.readLock().lock();
				try{
					for(SaveableListener l : m_SaveableListeners){
						l.update(m_Parent, event);
					}
				}finally{
					m_SaveableListenersLock.readLock().unlock();
				}
			}

		});
	}
	
	private String assignName(Data d){
		int count = 1;
		String name = d.getClass().getSimpleName()+String.valueOf(count);
		while(this.contains(name)){
			name = d.getClass().getSimpleName()+String.valueOf(++count);
		}
		return name;
	}
	
	private synchronized void addData(Data d, String name, boolean notify){
		m_DataLock.writeLock().lock();
		try{
			if(this.contains(d)){
				this.rename(d, this.getDataName(d), name, notify);
			}else{
				m_Data.add(d);
				m_NameToData.put(name, d);
				m_DataToName.put(d, name);
				if(notify){
					synchronized(m_EventThread){
						this.setChanged(true);
						this.notifyListeners(new WorkspaceEvent(Type.DATA_ADDED, d));
					}
				}
			}
		}finally{
			m_DataLock.writeLock().unlock();
		}
		
		
	}
	
	public void addData(Data d, String name){
		//synchronizes in this inner function call
		this.addData(d, name, true);
	}
	
	public synchronized void addData(Data d){
		//synchronize this, because assignName needs to be synchronized with the addData call.
		m_DataLock.writeLock().lock();
		try{
			this.addData(d, assignName(d));
		}finally{
			m_DataLock.writeLock().unlock();
		}
	}
	
	private void removeData(String name, Data d){
		m_DataLock.writeLock().lock();
		try{
			//no need to check if data is actually contained, that check happens in the public removeData methods
			m_Data.remove(d);
			m_NameToData.remove(name);
			m_DataToName.remove(d);
			synchronized(m_EventThread){
				this.setChanged(true);
				this.notifyListeners(new WorkspaceEvent(Type.DATA_REMOVED, d));
			}
		}finally{
			m_DataLock.writeLock().unlock();
		}
		//TODO - unclear whether data should be disposed here
		d.dispose();
	}
	
	public void removeData(String name){
		if(!this.contains(name)){
			return;
		}
		m_DataLock.writeLock().lock();
		try{
			if(this.contains(name)){
				this.removeData(name, this.getData(name));
			}
		}finally{
			m_DataLock.writeLock().unlock();
		}
	}
	
	public void removeData(Data d){
		if(!this.contains(d)){
			return;
		}
		m_DataLock.writeLock().lock();
		try{
			if(this.contains(d)){
				this.removeData(this.getDataName(d), d);
			}
		}finally{
			m_DataLock.writeLock().unlock();
		}
	}
	
	public List<Data> getAllData(){
		//return a copy of the data list to make sure there are no concurrency issues if the requester
		//wants to iterate over the list
		List<Data> copy;
		m_DataLock.readLock().lock();
		try{
			copy = new ArrayList<Data>(m_Data);
		}finally{
			m_DataLock.readLock().unlock();
		}
		return Collections.unmodifiableList(copy);
	}
	
	public Data getData(String name){
		Data d;
		m_DataLock.readLock().lock();
		try{
			d = m_NameToData.get(name);
		}finally{
			m_DataLock.readLock().unlock();
		}
		return d;
	}
	
	public String getDataName(Data d){
		String name;
		m_DataLock.readLock().lock();
		try{
			name = m_DataToName.get(d);
		}finally{
			m_DataLock.readLock().unlock();
		}
		return name;
	}
	
	public Collection<String> getDataNames(){
		//copy the names to avoid synchronization problems when iterating over the returned collection
		Collection<String> names;
		m_DataLock.readLock().lock();
		try{
			names = new ArrayList<String>(m_NameToData.keySet());
		}finally{
			m_DataLock.readLock().unlock();
		}
		return Collections.unmodifiableCollection(names);
	}
	
	private void rename(Data d, String oldName, String newName, boolean notify){
		m_DataLock.writeLock().lock();
		try{
			if(m_NameToData.containsKey(newName)){
				this.removeData(m_NameToData.get(newName));
			}
			m_NameToData.remove(oldName);
			m_NameToData.put(newName, d);
			m_DataToName.put(d, newName);
			this.notifyListeners(new WorkspaceEvent(Type.DATA_RENAMED, d));
			this.setChanged(true);
		}finally{
			m_DataLock.writeLock().unlock();
		}
	}
	
	protected boolean shouldRename(Data d, String name){
		boolean rename;
		m_DataLock.readLock().lock();
		try{
			rename = this.contains(d) && !this.getDataName(d).equals(name);
		}finally{
			m_DataLock.readLock().unlock();
		}
		return rename;
	}
	
	public void rename(Data d, String name){
		//check whether data should be renamed on the read lock, before trying to acquire the write lock
		if(!this.shouldRename(d, name)){
			return;
		}
		m_DataLock.writeLock().lock();
		try{
			//recheck shouldRead in case something changed in between releasing the read lock and acquiring the write lock
			if(this.shouldRename(d, name)){
				this.rename(d, this.getDataName(d), name, true);
			}
		}finally{
			m_DataLock.writeLock().unlock();
		}
	}
	
	public void rename(String oldName, String newName){
		//check that this contains oldName on the read lock, before trying to acquire the write lock
		if(!this.contains(oldName)){
			return;
		}
		m_DataLock.writeLock().lock();
		try{
			//recheck contains in case something changed in between releasing the read lock and acquiring the write lock
			if(this.contains(oldName)){
				this.rename(this.getData(oldName), oldName, newName, true);
			}
		}finally{
			m_DataLock.writeLock().unlock();
		}
	}

	public boolean contains(String name) {
		boolean result;
		m_DataLock.readLock().lock();
		try{
			result = m_NameToData.containsKey(name);
		}finally{
			m_DataLock.readLock().unlock();
		}
		return result;
	}

	public boolean contains(Data data) {
		boolean result;
		m_DataLock.readLock().lock();
		try{
			result = m_DataToName.containsKey(data);
		}finally{
			m_DataLock.readLock().unlock();
		}
		return result;
	}
	
	public void clear(){
		//first check if this is already cleared before acquiring the write lock
		if(this.isEmpty()){
			return;
		}
		m_DataLock.writeLock().lock();
		try{
			//state must be rechecked, because another thread might have acquired the write lock
			//and changed the state between the this.isEmpty() call and this thread acquiring the write lock
			boolean changed = !m_Data.isEmpty();
			m_Data.clear();
			m_NameToData.clear();
			m_DataToName.clear();
			if(changed){
				//synchronize on the event thread to ensure that these notifications go together and in order
				synchronized(m_EventThread){
					this.setChanged(true);
					this.notifyListeners(new WorkspaceEvent(Type.WORKSPACE_CLEARED));
				}
			}
		}finally{
			m_DataLock.writeLock().unlock();
		}
	}
	
	
	public boolean unsavedChanges(){
		boolean changed;
		m_ChangesLock.readLock().lock();
		try{
			changed = m_ChangesSinceLastSave;
		}finally{
			m_ChangesLock.readLock().unlock();
		}
		return changed;
	}
	
	private void setChanged(boolean changed){
		//check if this should be modified on read lock first before acquiring write lock
		if(changed == this.unsavedChanges()){
			return;
		}
		m_ChangesLock.writeLock().lock();
		try{
			//recheck in case something changed between releasing the read lock and acquiring the write lock
			if(changed != m_ChangesSinceLastSave){
				m_ChangesSinceLastSave = changed;
				this.notifyListeners(new SaveableEvent(SaveableEvent.Type.CHANGED));
			}
		}finally{
			m_ChangesLock.writeLock().unlock();
		}
	}
	
	@Override
	public long saveTo(OutputStream out, String outName) throws SaveException{
		m_DataLock.readLock().lock();
		long bytes;
		try{
			this.notifyListeners(new SaveableEvent(SaveableEvent.Type.SAVING, outName));
			ByteCounterOutputStream counter = new ByteCounterOutputStream(out);
			try {
				ObjectOutputStream oout = new ObjectOutputStream(counter);
				//first right the workspace name
				oout.writeObject(m_Name);
				//now write the stored data
				for(Data stored : m_Data){
					Bundle source = FrameworkUtil.getBundle(stored.getClass());
					String name = this.getDataName(stored);
					oout.writeObject(name);
					//recording the bundle name is necessary for the right class loader to
					//be used when deserializing the Data object
					oout.writeObject(source.getSymbolicName());
					oout.writeObject(stored);
				}
				this.setChanged(false);
				oout.close();
				this.notifyListeners(new SaveableEvent(SaveableEvent.Type.SAVED, outName));
			} catch (IOException e) {
				this.notifyListeners(new SaveableEvent(SaveableEvent.Type.FAILED, outName));
				throw new SaveException(e);
			}
			bytes = counter.bytesWritten();
		}finally{
			m_DataLock.readLock().unlock();
		}
		return bytes;
	}

	@Override
	public void loadFrom(InputStream in, String source) throws LoadException{
		m_DataLock.writeLock().lock();
		m_NameLock.writeLock().lock();
		try {
			//loading replaces the currently stored data, so clear
			this.clear();
			this.notifyListeners(new SaveableEvent(SaveableEvent.Type.LOADING, source));
			ClassLoaderObjectInputStream oin = new ClassLoaderObjectInputStream(in, this.getClass().getClassLoader());
			//first read the workspace name - this is why the name write lock must be held
			try {
				m_Name = (String) oin.readObject();
			} catch (ClassNotFoundException e1) {
				//zzzzzzzzz
			}
			//then read the data
			this.readDataFrom(oin);
			//this workspace was already saved, so set changed to false
			this.setChanged(false);
			this.notifyListeners(new WorkspaceEvent(Type.WORKSPACE_LOADED));
			oin.close();
			this.notifyListeners(new SaveableEvent(SaveableEvent.Type.LOADED, source));
		} catch (IOException e) {
			this.notifyListeners(new SaveableEvent(SaveableEvent.Type.FAILED, source));
			throw new LoadException(e);
		}finally{
			m_NameLock.writeLock().unlock();
			m_DataLock.writeLock().unlock();
		}
	}

	@Override
	public void importFrom(InputStream in, String sourceName) throws ImportException{
		m_DataLock.writeLock().lock();
		try {
			//importing reads in additional data, so the current data is not cleared
			this.notifyListeners(new SaveableEvent(SaveableEvent.Type.IMPORTING, sourceName));
			ClassLoaderObjectInputStream oin = new ClassLoaderObjectInputStream(in, this.getClass().getClassLoader());
			//read the imported workspace name, but ignore it
			try {
				oin.readObject();
			} catch (ClassNotFoundException e1) {
				//zzzzzzzzz
			}
			//read in the data
			this.readDataFrom(oin);
			this.notifyListeners(new WorkspaceEvent(Type.WORKSPACE_IMPORTED));
			oin.close();
			this.notifyListeners(new SaveableEvent(SaveableEvent.Type.IMPORTED, sourceName));
		} catch (IOException e) {
			this.notifyListeners(new SaveableEvent(SaveableEvent.Type.FAILED, sourceName));
			throw new ImportException(e);
		}finally{
			m_DataLock.writeLock().unlock();
		}
	}
	
	//this method is only called by methods holding the data write lock, so no need for synchronization
	private void readDataFrom(ClassLoaderObjectInputStream oin) {
		boolean finished = false;
		while(!finished){
			try {
				//set the class loader to the default class loader
				oin.setClassLoader(this.getClass().getClassLoader());
				String name = (String) oin.readObject();
				//read the name of the bundle that provided this data before
				//and see if it is available
				String bundleName = (String) oin.readObject();
				Bundle bundle = OSGIUtils.getBundleWithName(bundleName, m_Context);
				if(bundle!=null){
					//the bundle recorded as providing this data is available, so use its class loader
					//for deserializing the data
					oin.setClassLoader(OSGIUtils.getBundleClassLoader(bundle));
				}
				Data data = (Data) oin.readObject();
				//if importing there is still stored Data, so check to make sure that there will
				//be no naming collisions
				if(this.contains(name)){
					//there is a naming collision, so append numbers until an open name is found
					int count = 1;
					String tryName = name + "(" + count + ")";
					while(this.contains(tryName)){
						tryName = name + "(" + (++count) + ")";
					}
					name = tryName;
				}
				this.addData(data, name, false);
			} catch (ClassNotFoundException e) {
				//do nothing, this means the plugin that provides the data type is not loaded so simply proceed
				continue;
			} catch (Exception e){
				//an unknown error occured - the stream may have been fully read, so terminate the loop
				finished = true;
			}
		}
	}

	@Override
	public void addSaveableListener(SaveableListener l) {
		m_SaveableListenersLock.writeLock().lock();
		try{
			m_SaveableListeners.add(l);
		}finally{
			m_SaveableListenersLock.writeLock().unlock();
		}
	}

	@Override
	public void removeSaveableListener(SaveableListener l) {
		m_SaveableListenersLock.writeLock().lock();
		try{
			m_SaveableListeners.remove(l);
		}finally{
			m_SaveableListenersLock.writeLock().unlock();
		}
	}

	public void addWorkspaceListener(WorkspaceListener listener) {
		m_WorkspaceListenersLock.writeLock().lock();
		try{
			m_WorkspaceListeners.add(listener);
		}finally{
			m_WorkspaceListenersLock.writeLock().unlock();
		}
	}

	public void removeWorkspaceListener(WorkspaceListener listener) {
		m_WorkspaceListenersLock.writeLock().lock();
		try{
			m_WorkspaceListeners.remove(listener);
		}finally{
			m_WorkspaceListenersLock.writeLock().unlock();
		}
	}

	public synchronized String getWorkspaceName() {
		String name;
		m_NameLock.readLock().lock();
		try{
			name = m_Name;
		}finally{
			m_NameLock.readLock().unlock();
		}
		return name;
	}

	public synchronized void setWorkspaceName(String name) {
		//check that name should be updated on the read lock before acquiring write lock
		if(this.getWorkspaceName().equals(name)){
			return;
		}
		m_NameLock.writeLock().lock();
		try{
			if(!name.equals(m_Name)){
				m_Name = name;
				this.notifyListeners(new WorkspaceEvent(Type.WORKSPACE_RENAMED));
			}
		}finally{
			m_NameLock.writeLock().unlock();
		}
	}

	
	
	
	
	
	
}
