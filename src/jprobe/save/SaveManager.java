package jprobe.save;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import util.save.Saveable;
import util.save.SaveableEvent;
import util.save.SaveableEvent.Type;
import util.save.SaveableListener;
import jprobe.JProbeActivator;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeLog;
import jprobe.services.Workspace;

public class SaveManager implements Saveable, SaveableListener{
	
	private final ReadWriteLock m_SaveablesLock = new ReentrantReadWriteLock(false);
	private final ReadWriteLock m_ListenersLock = new ReentrantReadWriteLock(false);
	
	private final Map<String, Saveable> m_Saveables = new HashMap<String, Saveable>();
	private final Collection<SaveableListener> m_Listeners = new HashSet<SaveableListener>();
	
	private volatile boolean m_Changes = false;
	
	private final Workspace m_Parent;
	
	public SaveManager(Workspace parent){
		m_Parent = parent;
	}
	
	public void addSaveable(Saveable s, String tag){
		boolean changed = m_Changes;
		m_SaveablesLock.writeLock().lock();
		try{
			if(m_Saveables.containsKey(tag)){
				m_Saveables.get(tag).removeSaveableListener(this);
			}
			m_Saveables.put(tag, s);
			changed = changed || s.unsavedChanges();
			s.addSaveableListener(this);
		}finally{
			m_SaveablesLock.writeLock().unlock();
		}
		this.setChanged(changed);
	}
	
	public void removeSaveable(Saveable s, String tag){
		boolean removed = false;
		m_SaveablesLock.writeLock().lock();
		try{
			removed = m_Saveables.containsKey(tag) && m_Saveables.get(tag) == s;
			if(removed){
				Saveable remove = m_Saveables.remove(tag);
				remove.removeSaveableListener(this);
			}
		}finally{
			m_SaveablesLock.writeLock().unlock();
		}
		if(removed){
			this.updateChanges();
		}
	}
	
	private void notifyListeners(SaveableEvent event){
		m_ListenersLock.readLock().lock();
		try{
			for(SaveableListener l : m_Listeners){
				l.update(m_Parent, event);
			}
		}finally{
			m_ListenersLock.readLock().unlock();
		}
	}
	
	private void setChanged(boolean changed){
		if(changed != m_Changes){
			m_Changes = changed;
			this.notifyListeners(new SaveableEvent(Type.CHANGED));
		}
	}
	
	private void updateChanges(){
		boolean changed = false;
		m_SaveablesLock.readLock().lock();
		try{
			for(Saveable s : m_Saveables.values()){
				if(s.unsavedChanges()){
					changed = true;
					break;
				}
			}
		}finally{
			m_SaveablesLock.readLock().unlock();
		}
		this.setChanged(changed);
	}
	
	@Override
	public boolean unsavedChanges(){
		return m_Changes;
	}

	@Override
	public long saveTo(OutputStream out, String outName) {
		this.notifyListeners(new SaveableEvent(Type.SAVING, outName));
		long bytes = 0;
		m_SaveablesLock.readLock().lock();
		try{
			ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(out));
			bytes += SaveUtil.save(oout, outName, m_Saveables);
			m_SaveablesLock.readLock().unlock();
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "Saved workspace "+m_Parent.getWorkspaceName()+" to "+outName);
			this.notifyListeners(new SaveableEvent(Type.SAVED, outName));
		} catch (SaveException e){
			m_SaveablesLock.readLock().unlock();
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
			this.notifyListeners(new SaveableEvent(Type.FAILED, outName));
		} catch (Throwable t){
			m_SaveablesLock.readLock().unlock();
			ErrorHandler.getInstance().handleException(new RuntimeException(t), JProbeActivator.getBundle());
			this.notifyListeners(new SaveableEvent(Type.FAILED, outName));
		}
		return bytes;
	}

	@Override
	public void loadFrom(InputStream in, String sourceName) {
		this.notifyListeners(new SaveableEvent(Type.LOADING, sourceName));
		m_SaveablesLock.readLock().lock();
		try {
			ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
			SaveUtil.load(oin, sourceName, m_Saveables);
			m_SaveablesLock.readLock().unlock();
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "Opened workspace "+m_Parent.getWorkspaceName() + " from source "+sourceName);
			this.notifyListeners(new SaveableEvent(Type.LOADED, sourceName));
		} catch (LoadException e) {
			m_SaveablesLock.readLock().unlock();
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
			this.notifyListeners(new SaveableEvent(Type.FAILED, sourceName));
		} catch (Throwable t){
			m_SaveablesLock.readLock().unlock();
			ErrorHandler.getInstance().handleException(new RuntimeException(t), JProbeActivator.getBundle());
			this.notifyListeners(new SaveableEvent(Type.FAILED, sourceName));
		}
	}

	@Override
	public void importFrom(InputStream in, String sourceName) {
		this.notifyListeners(new SaveableEvent(Type.IMPORTING, sourceName));
		m_SaveablesLock.readLock().lock();
		try {
			ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
			SaveUtil.importFrom(oin, sourceName, m_Saveables);
			m_SaveablesLock.readLock().unlock();
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "Imported workspace from "+sourceName);
			this.notifyListeners(new SaveableEvent(Type.IMPORTED, sourceName));
		} catch (ImportException e) {
			m_SaveablesLock.readLock().unlock();
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
			this.notifyListeners(new SaveableEvent(Type.FAILED, sourceName));
		} catch (Throwable t){
			m_SaveablesLock.readLock().unlock();
			ErrorHandler.getInstance().handleException(new RuntimeException(t), JProbeActivator.getBundle());
			this.notifyListeners(new SaveableEvent(Type.FAILED, sourceName));
		}
	}

	@Override
	public void addSaveableListener(SaveableListener l) {
		m_ListenersLock.writeLock().lock();
		try{
			m_Listeners.add(l);
		}finally{
			m_ListenersLock.writeLock().unlock();
		}
	}

	@Override
	public void removeSaveableListener(SaveableListener l) {
		m_ListenersLock.writeLock().lock();
		try{
			m_Listeners.remove(l);
		}finally{
			m_ListenersLock.writeLock().unlock();
		}
	}

	@Override
	public void update(Saveable s, SaveableEvent event) {
		if(event.type == Type.CHANGED){
			this.updateChanges();
		}
	}
	
}
