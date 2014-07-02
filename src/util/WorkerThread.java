package util;

import java.util.LinkedList;

public class WorkerThread extends Thread{
	
	private static final String NAME = "WorkerThread-";
	private static int COUNT = 0;
	
	private final LinkedList<Runnable> m_ExternalTasks = new LinkedList<Runnable>();
	private final LinkedList<Runnable> m_InternalTasks = new LinkedList<Runnable>();
	
	private boolean m_Shutdown = false;
	
	public WorkerThread(){
		super();
		this.setName(this.assignName());
	}
	
	protected String assignName(){
		synchronized(WorkerThread.class){
			return NAME + (COUNT++);
		}
	}
	
	public synchronized boolean isShutdown(){
		return m_Shutdown;
	}
	
	public synchronized void shutdown(){
		m_Shutdown = true;
		this.notifyAll();
	}
	
	public synchronized void waitForShutdown() throws InterruptedException{
		this.join();
	}
	
	public synchronized void execute(Runnable r){
		if(Thread.currentThread() == this){
			m_InternalTasks.add(r);
			this.notifyAll();
		}else if(!this.isShutdown()){
			m_ExternalTasks.add(r);
			this.notifyAll();
		}
	}
	
	private synchronized boolean tasksRemaining(){
		return !m_ExternalTasks.isEmpty() && !m_InternalTasks.isEmpty();
	}
	
	private synchronized Runnable nextRunnable() throws InterruptedException{
		while(!this.tasksRemaining() && !this.isShutdown()){
			this.wait();
		}
		if(!m_InternalTasks.isEmpty()){
			return m_InternalTasks.poll();
		}
		if(!m_ExternalTasks.isEmpty()){
			return m_ExternalTasks.poll();
		}
		return null;
	}
	
	@Override
	public void run(){
		Runnable cur = null;
		while(!this.isShutdown() || this.tasksRemaining()){
			try {
				cur = this.nextRunnable();
			} catch (InterruptedException e) {
				throw new Error(e);
			}
			if(cur != null){
				cur.run();
			}
		}
	}
	
}	

