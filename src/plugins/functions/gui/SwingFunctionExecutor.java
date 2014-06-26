package plugins.functions.gui;

import javax.swing.SwingUtilities;

import org.osgi.framework.Bundle;

import util.gui.OnPress;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import jprobe.services.DataManager;
import jprobe.services.ErrorHandler;
import jprobe.services.data.Data;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionExecutor;

public class SwingFunctionExecutor<T> extends FunctionExecutor<T>{
	
	private class FunctionThread extends Thread implements ProgressListener{
		
		private final Function<T> m_Function;
		private final T m_Params;
		
		private FunctionThread(Function<T> func, T params){
			m_Function = func;
			m_Params = params;
		}
		
		@Override
		public void run(){
			try {
				final Data d = m_Function.execute(this, m_Params);
				done(d);
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, m_Bundle);
				done(null);
			} catch (Throwable t){
				//don't report this event, as canceling the thread will cause this to notify the user
				//with java.lang.ThreadDeath
				//ErrorHandler.getInstance().handleException(new RuntimeException(t), m_Bundle);
				done(null);
			}
		}

		@Override
		public void update(final ProgressEvent event) {
			switch(event.getType()){
			case UPDATE:
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						m_Monitor.setIndeterminate(event.isIndeterminant());
						m_Monitor.setMaxValue(event.getMaxProgress());
						m_Monitor.setText(event.getMessage());
						m_Monitor.setValue(event.getProgress());
					}
					
				});
				break;
			default:
				break;
			
			}
		}
		
	}
	
	private Bundle m_Bundle;
	private DataManager m_DataManager;
	private FunctionThread m_Thread;
	private ProgressWindow m_Monitor;

	public SwingFunctionExecutor(Function<T> function, T params, DataManager dataManager, Bundle bundle) {
		super(function);
		m_DataManager = dataManager;
		m_Bundle = bundle;
		m_Thread = new FunctionThread(this.getFunction(), params);
	}
	
	private void done(final Data d){
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				if(d != null){
					m_DataManager.addData(d, m_Bundle);
				}
				if(m_Monitor != null){
					m_Monitor.dispose();
					m_Monitor = null;
				}
			}
			
		});
	}

	@Override
	public void execute() {
		//this.monitor = new ProgressMonitor(null, thread.function.getName(), null, 0, PROGRESS_BOUND);
		m_Monitor = new ProgressWindow(m_Thread.m_Function.getName(), 0, 0, false, new OnPress(){

			@SuppressWarnings("deprecation")
			@Override
			public void act() {
				m_Thread.stop();
				done(null);
			}
			
		});
		m_Thread.start();
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Data getResults() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
