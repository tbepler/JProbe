package jprobe;

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

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import util.ByteCounterOutputStream;
import util.ClassLoaderObjectInputStream;
import util.OSGIUtils;
import util.save.Saveable;
import util.save.SaveableEvent;
import util.save.SaveableListener;
import jprobe.services.data.Data;
import jprobe.services.Workspace;
import jprobe.services.ErrorHandler;
import jprobe.services.WorkspaceEvent;
import jprobe.services.WorkspaceEvent.Type;
import jprobe.services.WorkspaceListener;

public class DataManager implements Saveable{
	
	private final Collection<WorkspaceListener> m_WorkspaceListeners = new HashSet<WorkspaceListener>();
	private final Collection<SaveableListener> m_SaveableListeners = new HashSet<SaveableListener>();
	
	private final List<Data> m_Data = new ArrayList<Data>();
	private final Map<String, Data> m_NameToData = new HashMap<String, Data>();
	private final Map<Data, String> m_DataToName = new HashMap<Data, String>();
	
	private final BundleContext m_Context;
	private final Workspace m_Parent;
	
	private boolean m_ChangesSinceLastSave = false;
	private String m_Name;
	
	public DataManager(BundleContext context, Workspace parent, String name){
		m_Context = context;
		m_Parent = parent;
		m_Name = name;
	}
	
	public synchronized boolean isEmpty(){
		return m_Data.isEmpty();
	}

	protected synchronized void notifyListeners(WorkspaceEvent event){
		for(WorkspaceListener l : m_WorkspaceListeners){
			l.update(m_Parent, event);
		}
	}
	
	protected synchronized void notifyListeners(SaveableEvent event){
		for(SaveableListener l : m_SaveableListeners){
			l.update(this, event);
		}
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
		if(this.contains(d)){
			this.rename(d, this.getDataName(d), name, notify);
		}else{
			m_Data.add(d);
			m_NameToData.put(name, d);
			m_DataToName.put(d, name);
			if(notify){
				this.notifyListeners(new WorkspaceEvent(Type.DATA_ADDED, d));
				this.setChanged(true);
			}
		}
	}
	
	public synchronized void addData(Data d, String name){
		this.addData(d, name, true);
	}
	
	public synchronized void addData(Data d){
		this.addData(d, assignName(d));
	}
	
	private void removeData(String name, Data d){
		m_Data.remove(d);
		m_NameToData.remove(name);
		m_DataToName.remove(d);
		//TODO
		d.dispose();
		this.notifyListeners(new WorkspaceEvent(Type.DATA_REMOVED, d));
		this.setChanged(true);
	}
	
	public synchronized void removeData(String name){
		removeData(name, m_NameToData.get(name));
	}
	
	public synchronized void removeData(Data d){
		removeData(m_DataToName.get(d), d);
	}
	
	public synchronized List<Data> getAllData(){
		return Collections.unmodifiableList(m_Data);
	}
	
	public synchronized Data getData(String name){
		return m_NameToData.get(name);
	}
	
	public synchronized String getDataName(Data d){
		return m_DataToName.get(d);
	}
	
	public synchronized Collection<String> getDataNames(){
		return Collections.unmodifiableCollection(m_NameToData.keySet());
	}
	
	private synchronized void rename(Data d, String oldName, String newName, boolean notify){
		if(m_NameToData.containsKey(newName)){
			this.removeData(newName);
		}
		m_NameToData.remove(oldName);
		m_NameToData.put(newName, d);
		m_DataToName.put(d, newName);
		if(notify){
			this.notifyListeners(new WorkspaceEvent(Type.DATA_RENAMED, d));
			this.setChanged(true);
		}
	}
	
	public synchronized void rename(Data d, String name){
		this.rename(d, this.getDataName(d), name, true);
	}
	
	public synchronized void rename(String oldName, String newName){
		Data d = this.getData(oldName);
		if(d != null){
			this.rename(d, oldName, newName, true);
		}
	}

	public synchronized boolean contains(String name) {
		return m_NameToData.containsKey(name);
	}

	public synchronized boolean contains(Data data) {
		return m_DataToName.containsKey(data);
	}
	
	public synchronized void clear(){
		boolean changed = !m_Data.isEmpty();
		m_Data.clear();
		m_NameToData.clear();
		m_DataToName.clear();
		this.notifyListeners(new WorkspaceEvent(Type.WORKSPACE_CLEARED));
		this.setChanged(changed);
	}
	
	public synchronized boolean unsavedChanges(){
		return m_ChangesSinceLastSave;
	}
	
	private void setChanged(boolean changed){
		if(changed != m_ChangesSinceLastSave){
			m_ChangesSinceLastSave = changed;
			this.notifyListeners(new SaveableEvent(SaveableEvent.Type.CHANGED));
		}
	}
	
	@Override
	public synchronized long saveTo(OutputStream out, String outName) {
		this.notifyListeners(new SaveableEvent(SaveableEvent.Type.SAVING, outName));
		ByteCounterOutputStream counter = new ByteCounterOutputStream(out);
		try {
			ObjectOutputStream oout = new ObjectOutputStream(counter);
			//first right the workspace name
			oout.writeObject(m_Name);
			//now right the stored data
			for(Data stored : m_DataToName.keySet()){
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
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
		return counter.bytesWritten();
	}

	@Override
	public synchronized void loadFrom(InputStream in, String source) {
		try {
			//loading replaces the currently stored data, so clear
			this.clear();
			this.notifyListeners(new SaveableEvent(SaveableEvent.Type.LOADING, source));
			ClassLoaderObjectInputStream oin = new ClassLoaderObjectInputStream(in, this.getClass().getClassLoader());
			//first read the workspace name
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
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
	}

	@Override
	public synchronized void importFrom(InputStream in, String sourceName) {
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
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
	}
	
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
	public synchronized void addSaveableListener(SaveableListener l) {
		m_SaveableListeners.add(l);
	}

	@Override
	public synchronized void removeSaveableListener(SaveableListener l) {
		m_SaveableListeners.remove(l);
	}

	public synchronized void addWorkspaceListener(WorkspaceListener listener) {
		m_WorkspaceListeners.add(listener);
	}

	public synchronized void removeWorkspaceListener(WorkspaceListener listener) {
		m_WorkspaceListeners.remove(listener);
	}

	public synchronized String getWorkspaceName() {
		return m_Name;
	}

	public synchronized void setWorkspaceName(String name) {
		if(!name.equals(m_Name)){
			m_Name = name;
			this.notifyListeners(new WorkspaceEvent(Type.WORKSPACE_RENAMED));
		}
	}

	
	
	
	
	
	
}
