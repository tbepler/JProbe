package jprobe.save;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import jprobe.JProbeActivator;
import jprobe.services.ErrorHandler;
import jprobe.services.LoadEvent;
import jprobe.services.LoadEvent.Type;
import jprobe.services.LoadListener;
import jprobe.services.Log;
import jprobe.services.SaveEvent;
import jprobe.services.SaveListener;
import jprobe.services.Saveable;

public class SaveManager{
	
	private final ReadWriteLock m_SaveablesLock = new ReentrantReadWriteLock(false);

	private final Map<String, Saveable> m_Saveables = new HashMap<String, Saveable>();
	private final Collection<SaveListener> m_SaveLs = new HashSet<SaveListener>();
	private final Collection<LoadListener> m_LoadLs = new HashSet<LoadListener>();

	public void addSaveable(Saveable s, String tag){
		m_SaveablesLock.writeLock().lock();
		try{
			m_Saveables.put(tag, s);
		}finally{
			m_SaveablesLock.writeLock().unlock();
		}
	}
	
	public void removeSaveable(Saveable s, String tag){
		m_SaveablesLock.writeLock().lock();
		try{
			if(m_Saveables.containsKey(tag) && m_Saveables.get(tag) == s){
				m_Saveables.remove(tag);
			}
		}finally{
			m_SaveablesLock.writeLock().unlock();
		}
	}
	
	public boolean changesSinceSave(){
		boolean result = false;
		m_SaveablesLock.readLock().lock();
		try{
			for(Saveable s : m_Saveables.values()){
				if(s.changedSinceSave()){
					result = true;
					break;
				}
			}
		}finally{
			m_SaveablesLock.readLock().unlock();
		}
		return result;
	}
	
	public void save(File saveTo){
		this.notifySave(new SaveEvent(SaveEvent.Type.SAVING, saveTo));
		m_SaveablesLock.readLock().lock();
		try{
			SaveUtil.save(saveTo, m_Saveables);
			m_SaveablesLock.readLock().unlock();
			Log.getInstance().write(JProbeActivator.getBundle(), "Saved workspace to file "+saveTo);
			this.notifySave(new SaveEvent(SaveEvent.Type.SAVED, saveTo));
		} catch (SaveException e){
			m_SaveablesLock.readLock().unlock();
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
			this.notifySave(new SaveEvent(SaveEvent.Type.FAILED, saveTo));
		} catch (Throwable t){
			m_SaveablesLock.readLock().unlock();
			ErrorHandler.getInstance().handleException(new RuntimeException(t), JProbeActivator.getBundle());
			this.notifySave(new SaveEvent(SaveEvent.Type.FAILED, saveTo));
		}
	}
	
	public void load(File loadFrom){
		this.notifyLoad(new LoadEvent(Type.LOADING, loadFrom));
		m_SaveablesLock.readLock().lock();
		try {
			SaveUtil.load(loadFrom, m_Saveables);
			m_SaveablesLock.readLock().unlock();
			Log.getInstance().write(JProbeActivator.getBundle(), "Opened workspace "+loadFrom);
			this.notifyLoad(new LoadEvent(Type.LOADED, loadFrom));
		} catch (LoadException e) {
			m_SaveablesLock.readLock().unlock();
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
			this.notifyLoad(new LoadEvent(Type.FAILED, loadFrom));
		} catch (Throwable t){
			m_SaveablesLock.readLock().unlock();
			ErrorHandler.getInstance().handleException(new RuntimeException(t), JProbeActivator.getBundle());
			this.notifyLoad(new LoadEvent(Type.FAILED, loadFrom));
		}
	}

	public void registerLoad(LoadListener l){
		synchronized(m_LoadLs){
			m_LoadLs.add(l);
		}
	}

	public void unregisterLoad(LoadListener l){
		synchronized(m_LoadLs){
			m_LoadLs.remove(l);
		}
	}

	protected void notifyLoad(LoadEvent e){
		synchronized(m_LoadLs){
			for(LoadListener l : m_LoadLs){
				l.update(e);
			}
		}
	}

	public void registerSave(SaveListener l){
		synchronized(m_SaveLs){
			m_SaveLs.add(l);
		}
	}

	public void unregisterSave(SaveListener l){
		synchronized(m_SaveLs){
			m_SaveLs.remove(l);
		}
	}

	protected void notifySave(SaveEvent e){
		synchronized(m_SaveLs){
			for(SaveListener l : m_SaveLs){
				l.update(e);
			}
		}
	}
	
}
