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

import util.WorkerThread;
import util.save.ImportException;
import util.save.LoadException;
import util.save.SaveException;
import util.save.Saveable;
import util.save.SaveableEvent;
import util.save.SaveableEvent.Type;
import util.save.SaveableListener;
import jprobe.JProbeActivator;
import jprobe.services.JProbeLog;
import jprobe.services.Workspace;

public class SaveManager implements Saveable, SaveableListener{
	
	private final ReadWriteLock m_SaveablesLock = new ReentrantReadWriteLock(true);
	private final ReadWriteLock m_ListenersLock = new ReentrantReadWriteLock(true);
	private final ReadWriteLock m_ChangeLock = new ReentrantReadWriteLock(true);
	
	private final Map<String, Saveable> m_Saveables = new HashMap<String, Saveable>();
	private final Collection<SaveableListener> m_Listeners = new HashSet<SaveableListener>();
	
	private final WorkerThread m_EventThread;
	
	private volatile boolean m_Changes = false;
	
	private final Workspace m_Parent;
	
	public SaveManager(Workspace parent, WorkerThread eventThread){
		m_Parent = parent;
		m_EventThread = eventThread;
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
			this.setChanged(changed);
			s.addSaveableListener(this);
		}finally{
			m_SaveablesLock.writeLock().unlock();
		}
	}
	
	public void removeSaveable(Saveable s, String tag){
		boolean removed = false;
		m_SaveablesLock.writeLock().lock();
		try{
			removed = m_Saveables.containsKey(tag) && m_Saveables.get(tag) == s;
			if(removed){
				Saveable remove = m_Saveables.remove(tag);
				remove.removeSaveableListener(this);
				this.updateChanges();
			}
		}finally{
			m_SaveablesLock.writeLock().unlock();
		}
	}
	
	private void notifyListeners(final SaveableEvent event){
		m_EventThread.execute(new Runnable(){

			@Override
			public void run() {
				m_ListenersLock.readLock().lock();
				try{
					for(SaveableListener l : m_Listeners){
						l.update(m_Parent, event);
					}
				}finally{
					m_ListenersLock.readLock().unlock();
				}
			}
			
		});
		
	}
	
	private void setChanged(boolean changed){
		if(changed == this.unsavedChanges()){
			return;
		}
		m_ChangeLock.writeLock().lock();
		try{
			if(changed != m_Changes){
				m_Changes = changed;
				this.notifyListeners(new SaveableEvent(Type.CHANGED));
			}
		}finally{
			m_ChangeLock.writeLock().unlock();
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
			this.setChanged(changed);
		}finally{
			m_SaveablesLock.readLock().unlock();
		}
	}
	
	@Override
	public boolean unsavedChanges(){
		boolean changes;
		m_ChangeLock.readLock().lock();
		try{
			changes = m_Changes;
		}finally{
			m_ChangeLock.readLock().unlock();
		}
		return changes;
	}

	@Override
	public long saveTo(OutputStream out, String outName) throws SaveException{
		SaveableEvent start = new SaveableEvent(Type.SAVING, outName);
		this.notifyListeners(start);
		long bytes = 0;
		m_SaveablesLock.readLock().lock();
		try{
			ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(out));
			bytes += SaveUtil.save(oout, outName, m_Saveables);
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "Saved workspace "+m_Parent.getWorkspaceName()+" to "+outName);
			this.notifyListeners(new SaveableEvent(Type.SAVED, outName, start));
		} catch (SaveException e){
			this.notifyListeners(new SaveableEvent(Type.FAILED, outName, start));
			throw e;
		} catch (Throwable t){
			this.notifyListeners(new SaveableEvent(Type.FAILED, outName, start));
			throw new SaveException(t);
		}finally{
			m_SaveablesLock.readLock().unlock();
		}
		return bytes;
	}

	@Override
	public void loadFrom(InputStream in, String sourceName) throws LoadException{
		SaveableEvent start = new SaveableEvent(Type.LOADING, sourceName);
		this.notifyListeners(start);
		m_SaveablesLock.readLock().lock();
		try {
			ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
			SaveUtil.load(oin, sourceName, m_Saveables);
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "Opened workspace "+m_Parent.getWorkspaceName() + " from source "+sourceName);
			this.notifyListeners(new SaveableEvent(Type.LOADED, sourceName, start));
		} catch (LoadException e) {
			this.notifyListeners(new SaveableEvent(Type.FAILED, sourceName, start));
			throw e;
		} catch (Throwable t){
			this.notifyListeners(new SaveableEvent(Type.FAILED, sourceName, start));
			throw new LoadException(t);
		}finally{
			m_SaveablesLock.readLock().unlock();
		}
	}

	@Override
	public void importFrom(InputStream in, String sourceName) throws ImportException{
		SaveableEvent start = new SaveableEvent(Type.IMPORTING, sourceName);
		this.notifyListeners(start);
		m_SaveablesLock.readLock().lock();
		try {
			ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
			SaveUtil.importFrom(oin, sourceName, m_Saveables);
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "Imported workspace from "+sourceName);
			this.notifyListeners(new SaveableEvent(Type.IMPORTED, sourceName, start));
		} catch (ImportException e) {
			this.notifyListeners(new SaveableEvent(Type.FAILED, sourceName, start));
			throw e;
		} catch (Throwable t){
			this.notifyListeners(new SaveableEvent(Type.FAILED, sourceName, start));
			throw new ImportException(t);
		}finally{
			m_SaveablesLock.readLock().unlock();
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
