package jprobe;

import java.io.File;
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

public class DataManager implements Workspace{
	
	private final Collection<WorkspaceListener> m_WorkspaceListeners = new HashSet<WorkspaceListener>();
	private final Collection<SaveableListener> m_SaveableListeners = new HashSet<SaveableListener>();
	private final Map<String, Saveable> m_Saveables = new HashMap<String, Saveable>();
	
	private final List<Data> m_Data = new ArrayList<Data>();
	private final Map<String, Data> m_NameToData = new HashMap<String, Data>();
	private final Map<Data, String> m_DataToName = new HashMap<Data, String>();
	
	private final BundleContext m_Context;
	
	private boolean m_ChangesSinceLastSave = false;
	private String m_Name;
	
	public DataManager(BundleContext context, String name){
		m_Context = context;
		m_Name = name;
	}

	protected synchronized void notifyListeners(WorkspaceEvent event){
		for(WorkspaceListener l : m_WorkspaceListeners){
			l.update(this, event);
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
	
	@Override
	public synchronized void addData(Data d, String name){
		this.addData(d, name, true);
	}
	
	@Override
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
	
	@Override
	public synchronized void removeData(String name){
		removeData(name, m_NameToData.get(name));
	}
	
	@Override
	public synchronized void removeData(Data d){
		removeData(m_DataToName.get(d), d);
	}
	
	@Override
	public synchronized List<Data> getAllData(){
		return Collections.unmodifiableList(m_Data);
	}
	
	@Override
	public synchronized Data getData(String name){
		return m_NameToData.get(name);
	}
	
	@Override
	public synchronized String getDataName(Data d){
		return m_DataToName.get(d);
	}
	
	@Override
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
	
	@Override
	public synchronized void rename(Data d, String name){
		this.rename(d, this.getDataName(d), name, true);
	}
	
	@Override
	public synchronized void rename(String oldName, String newName){
		Data d = this.getData(oldName);
		if(d != null){
			this.rename(d, oldName, newName, true);
		}
	}


	@Override
	public synchronized boolean contains(String name) {
		return m_NameToData.containsKey(name);
	}

	@Override
	public synchronized boolean contains(Data data) {
		return m_DataToName.containsKey(data);
	}
	
	@Override
	public synchronized void clear(){
		boolean changed = !m_Data.isEmpty();
		m_Data.clear();
		m_NameToData.clear();
		m_DataToName.clear();
		this.notifyListeners(new WorkspaceEvent(Type.WORKSPACE_CLEARED));
		this.setChanged(changed);
	}
	
	@Override
	public synchronized boolean unsavedChanges(){
		return m_ChangesSinceLastSave;
	}
	
	private void setChanged(boolean changed){
		if(changed != m_ChangesSinceLastSave){
			m_ChangesSinceLastSave = changed;
			if(m_ChangesSinceLastSave){
				this.notifyListeners(new SaveableEvent(SaveableEvent.Type.CHANGED));
			}
		}
	}
	

	@Override
	public void saveTo(File saveTo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadFrom(File loadFrom) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importFrom(File f) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized long saveTo(OutputStream out, String outName) {
		ByteCounterOutputStream counter = new ByteCounterOutputStream(out);
		try {
			ObjectOutputStream oout = new ObjectOutputStream(counter);
			oout.writeObject(m_Name);
			for(Data stored : m_DataToName.keySet()){
				Bundle source = FrameworkUtil.getBundle(stored.getClass());
				String name = this.getDataName(stored);
				oout.writeObject(name);
				oout.writeObject(source.getSymbolicName());
				oout.writeObject(stored);
			}
			this.setChanged(false);
			oout.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());	
		}
		return counter.bytesWritten();
	}

	@Override
	public synchronized void loadFrom(InputStream in, String source) {
		try {
			this.clear();
			ClassLoaderObjectInputStream oin = new ClassLoaderObjectInputStream(in, this.getClass().getClassLoader());
			try {
				m_Name = (String) oin.readObject();
			} catch (ClassNotFoundException e1) {
				//zzzzzzzzz
			}
			boolean finished = false;
			while(!finished){
				try {
					oin.setClassLoader(this.getClass().getClassLoader());
					String name = (String) oin.readObject();
					String bundleName = (String) oin.readObject();
					Bundle bundle = OSGIUtils.getBundleWithName(bundleName, m_Context);
					if(bundle!=null){
						oin.setClassLoader(OSGIUtils.getBundleClassLoader(bundle));
					}
					Data data = (Data) oin.readObject();
					this.addData(data, name, false);
				} catch (ClassNotFoundException e) {
					//do nothing, this means the plugin that provides the data type is not loaded so simply proceed
					continue;
				} catch (Exception e){
					finished = true;
				}
			}
			this.setChanged(false);
			this.notifyListeners(new WorkspaceEvent(Type.WORKSPACE_LOADED));
			oin.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
	}

	@Override
	public synchronized void importFrom(InputStream in, String sourceName) {
		try {
			ClassLoaderObjectInputStream oin = new ClassLoaderObjectInputStream(in, this.getClass().getClassLoader());
			try {
				oin.readObject(); //read the imported workspace name, but ignore it
			} catch (ClassNotFoundException e1) {
				//zzzzzzzzz
			}
			boolean finished = false;
			while(!finished){
				try {
					oin.setClassLoader(this.getClass().getClassLoader());
					String name = (String) oin.readObject();
					String bundleName = (String) oin.readObject();
					Bundle bundle = OSGIUtils.getBundleWithName(bundleName, m_Context);
					if(bundle!=null){
						oin.setClassLoader(OSGIUtils.getBundleClassLoader(bundle));
					}
					Data data = (Data) oin.readObject();
					this.addData(data, name, false);
				} catch (ClassNotFoundException e) {
					//do nothing, this means the plugin that provides the data type is not loaded so simply proceed
					continue;
				} catch (Exception e){
					finished = true;
				}
			}
			this.notifyListeners(new WorkspaceEvent(Type.WORKSPACE_IMPORTED));
			oin.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
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

	@Override
	public synchronized void addWorkspaceListener(WorkspaceListener listener) {
		m_WorkspaceListeners.add(listener);
	}

	@Override
	public synchronized void removeWorkspaceListener(WorkspaceListener listener) {
		m_WorkspaceListeners.remove(listener);
	}

	@Override
	public synchronized String getWorkspaceName() {
		return m_Name;
	}

	@Override
	public synchronized void setWorkspaceName(String name) {
		if(!name.equals(m_Name)){
			m_Name = name;
			this.notifyListeners(new WorkspaceEvent(Type.WORKSPACE_RENAMED));
		}
	}

	@Override
	public synchronized void putSaveable(String tag, Saveable s) {
		m_Saveables.put(tag, s);
	}

	@Override
	public synchronized void removeSaveable(String tag, Saveable s) {
		if(m_Saveables.containsKey(tag) && m_Saveables.get(tag) == s){
			m_Saveables.remove(tag);
		}
	}

	
	
	
	
	
	
}
