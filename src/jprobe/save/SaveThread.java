package jprobe.save;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import jprobe.JProbeActivator;
import jprobe.services.ErrorHandler;
import jprobe.services.Log;
import jprobe.services.Saveable;

public class SaveThread extends Thread{
	
	private final Lock m_FilesLock = new ReentrantLock(false);
	private final ReadWriteLock m_SaveablesLock = new ReentrantReadWriteLock(false);
	
	private final Set<File> m_Files = new HashSet<File>();
	private final Queue<File> m_Queue = new LinkedList<File>();
	
	private final Map<String, Saveable> m_Saveables = new HashMap<String, Saveable>();
	
	private volatile boolean m_Terminated = false;
	private volatile boolean m_Suspended = false;
	
	public SaveThread(){
		super("SaveThread");
	}
	
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
	
	public Map<String, Saveable> getSaveables(){
		Map<String, Saveable> copy;
		m_SaveablesLock.readLock().lock();
		try{
			copy = new HashMap<String, Saveable>(m_Saveables);
		}finally{
			m_SaveablesLock.readLock().unlock();
		}
		return copy;
	}
	
	/**
	 * Terminates this save thread. All pending saves will be processed and then the thread will terminate.
	 */
	public void terminate(){
		if(!m_Terminated){
			m_Terminated = true;
			synchronized(this){
				this.notifyAll();
			}
		}
	}
	
	private boolean isFlushed(){
		m_FilesLock.lock();
		boolean result = m_Files.isEmpty();
		m_FilesLock.unlock();
		return result;
	}
	
	/**
	 * Suspends this save thread, preventing new save requests from being accepted, and blocks
	 * until all pending save requests have been processed.
	 */
	public void suspendAndFlush(){
		m_Suspended = true;
		synchronized(this){
			while(!this.isFlushed()){
				try {
					this.wait();
				} catch (InterruptedException e) {
					ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
				}
			}
		}
	}
	
	/**
	 * Resumes this save thread if it was suspended
	 */
	public void proceed(){
		if(m_Suspended){
			m_Suspended = false;
		}
	}
	
	/**
	 * Issues a new save request to this thread. Returns true if the save request was successfully issued
	 * and false otherwise.
	 * @param f - File to which to save
	 * @return - true if request successfully issued
	 */
	public boolean save(File f){
		if(m_Suspended || m_Terminated) return false;
		try {
			File canonical = f.getCanonicalFile();
			boolean added = false;
			m_FilesLock.lock();
			try{
				if(!m_Files.contains(canonical)){
					m_Queue.add(canonical);
					m_Files.add(canonical);
					added = true;
				}
			}finally{
				m_FilesLock.unlock();
			}
			if(added){
				synchronized(this){
					this.notifyAll();
				}
			}
			return added;
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
			return false;
		}
	}
	
	@Override
	public void run(){
		while(!m_Terminated || !this.isFlushed()){
			//spin while there are no files to process
			synchronized(this){
				while(this.isFlushed() && !m_Terminated){
					this.notifyAll();
					try {
						this.wait();
					} catch (InterruptedException e) {
						ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
					}
				}
			}
			//process the next save request
			m_FilesLock.lock();
			File saveTo = m_Queue.poll();
			m_FilesLock.unlock();

			if(saveTo != null){
				m_SaveablesLock.readLock().lock();
				try {
					SaveUtil.save(saveTo, m_Saveables);
					Log.getInstance().write(JProbeActivator.getBundle(), "Saved to file "+saveTo);
				} catch (SaveException e) {
					ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
				} finally{
					m_SaveablesLock.readLock().unlock();
				}
			}
			
			m_FilesLock.lock();
			m_Files.remove(saveTo);
			m_FilesLock.unlock();
		}
	}


	
}
