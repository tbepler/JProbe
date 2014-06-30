package plugins.jprobe.gui;

import java.util.LinkedList;
import java.util.Queue;
import jprobe.services.ErrorHandler;

public class BackgroundThread extends Thread{
	
	public BackgroundThread(String name){
		super(name);
	}
	
	private final Queue<Runnable> m_Actions = new LinkedList<Runnable>();
	private volatile boolean m_Terminated = false;
	
	public synchronized void invokeLater(Runnable r){
		if(m_Terminated) return; //prevent new actions from being queued if this thread should be terminated
		m_Actions.add(r);
		this.notifyAll();
	}
	
	public synchronized void terminate(){
		m_Terminated = true;
		this.notifyAll();
	}
	
	public boolean isTerminated(){
		return m_Terminated;
	}
	
	private synchronized boolean proceed(){
		return !m_Terminated || !m_Actions.isEmpty();
	}
	
	private synchronized Runnable getNextAction(){
		while(m_Actions.isEmpty() && !m_Terminated){
			try {
				this.wait();
			} catch (InterruptedException e) {
				ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
			}
		}
		return m_Actions.poll();
	}
	
	
	@Override
	public void run(){
		while(this.proceed()){
			Runnable process = this.getNextAction();
			if(process != null){
				try{
					process.run();
				} catch(Exception e){
					ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
				} catch(Throwable t){
					ErrorHandler.getInstance().handleException(new RuntimeException(t), GUIActivator.getBundle());
				}
			}
		}
	}
	
}
