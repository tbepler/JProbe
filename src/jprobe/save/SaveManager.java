package jprobe.save;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import jprobe.JProbeActivator;
import jprobe.services.ErrorHandler;
import jprobe.services.LoadEvent;
import jprobe.services.LoadEvent.Type;
import jprobe.services.LoadListener;
import jprobe.services.Log;
import jprobe.services.SaveEvent;
import jprobe.services.SaveListener;
import jprobe.services.Saveable;

public class SaveManager implements SaveListener{
	
	private final SaveThread m_SaveThread = new SaveThread();
	private final Collection<SaveListener> m_Listeners = new HashSet<SaveListener>();
	private final Collection<LoadListener> m_LoadLs = new HashSet<LoadListener>();
	
	public void start(){
		m_SaveThread.register(this);
		m_SaveThread.start();
	}
	
	public void addSaveable(Saveable s, String tag){
		m_SaveThread.addSaveable(s, tag);
	}
	
	public void removeSaveable(Saveable s, String tag){
		m_SaveThread.removeSaveable(s, tag);
	}
	
	public void flushAndSuspend(){
		m_SaveThread.suspendAndFlush();
	}
	
	public void resume(){
		m_SaveThread.proceed();
	}
	
	public void terminate(){
		m_SaveThread.terminate();
		try {
			m_SaveThread.join();
		} catch (InterruptedException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
	}
	
	public boolean changesSinceSave(){
		return m_SaveThread.changesSinceSave();
	}
	
	public void save(File saveTo){
		m_SaveThread.save(saveTo);
	}
	
	public void load(File loadFrom){
		m_SaveThread.suspendAndFlush();
		this.notifyLoad(new LoadEvent(Type.LOADING, loadFrom));
		try {
			SaveUtil.load(loadFrom, m_SaveThread.getSaveables());
			Log.getInstance().write(JProbeActivator.getBundle(), "Opened workspace "+loadFrom);
			this.notifyLoad(new LoadEvent(Type.LOADED, loadFrom));
		} catch (LoadException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
			this.notifyLoad(new LoadEvent(Type.FAILED, loadFrom));
		}
		m_SaveThread.proceed();
	}
	
	public void registerLoad(LoadListener l){
		m_LoadLs.add(l);
	}
	
	public void unregisterLoad(LoadListener l){
		m_LoadLs.remove(l);
	}
	
	protected void notifyLoad(LoadEvent e){
		for(LoadListener l : m_LoadLs){
			l.update(e);
		}
	}
	
	public void registerSave(SaveListener l){
		m_Listeners.add(l);
	}
	
	public void unregisterSave(SaveListener l){
		m_Listeners.remove(l);
	}
	
	@Override
	public void update(SaveEvent e) {
		for(SaveListener l : m_Listeners){
			l.update(e);
		}
	}
	
}
