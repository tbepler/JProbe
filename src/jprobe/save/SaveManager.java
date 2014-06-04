package jprobe.save;

import java.io.File;

import jprobe.JProbeActivator;
import jprobe.services.ErrorHandler;
import jprobe.services.Log;
import jprobe.services.Saveable;

public class SaveManager {
	
	private final SaveThread m_SaveThread = new SaveThread();
	
	public void start(){
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
		try {
			SaveUtil.load(loadFrom, m_SaveThread.getSaveables());
			Log.getInstance().write(JProbeActivator.getBundle(), "Opened workspace "+loadFrom);
		} catch (LoadException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
		m_SaveThread.proceed();
	}
	
}
