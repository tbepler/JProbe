package plugins.functions.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingWorker;

import org.osgi.framework.Bundle;

import util.gui.OnPress;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import jprobe.services.DataManager;
import jprobe.services.ErrorHandler;
import jprobe.services.data.Data;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionExecutor;

public class SwingFunctionExecutor<T> extends FunctionExecutor<T> implements PropertyChangeListener{
	
	public static final int PROGRESS_BOUND = 100;
	
	class FunctionThread extends SwingWorker<Data, Data> implements ProgressListener{
		
		private Function<T> m_Function;
		private T m_Params;
		private int m_MaxProgress = PROGRESS_BOUND;
		private String m_Text = null;
		private boolean m_Indeterminate = false;
		
		FunctionThread(Function<T> function, T params){
			m_Function = function;
			m_Params = params;
		}
		
		@Override
		public void update(ProgressEvent event) {
			switch(event.getType()){
			case UPDATE:
				if(event.getMaxProgress() > 0)
					m_MaxProgress = event.getMaxProgress();
				m_Indeterminate = event.isIndeterminant();
				m_Text = event.getMessage();
				this.setProgress(event.getProgress());
				break;
			default:
				break;
			}
		}
		
		public int getMax(){ return m_MaxProgress; }
		
		public String getText(){ return m_Text; }
		
		public boolean isIndeterminate(){ return m_Indeterminate; }
		
		@Override
		protected void done(){
			if(this.isCancelled()){
				setCancelled();
				return;
			}
			try{
				setResults(this.get());
				setComplete();
			} catch (Exception e){
				ErrorHandler.getInstance().handleException(e, m_Bundle);
			}
		}

		@Override
		protected Data doInBackground() throws Exception {
			return m_Function.execute(this, m_Params);
		}
		
	}
	
	private boolean m_Completed;
	private boolean m_Cancelled;
	private Bundle m_Bundle;
	private DataManager m_DataManager;
	private Data m_Result;
	private FunctionThread m_Thread;
	private ProgressWindow m_Monitor;
	//private ProgressMonitor monitor;

	public SwingFunctionExecutor(Function<T> function, T params, DataManager dataManager, Bundle bundle) {
		super(function);
		m_DataManager = dataManager;
		m_Bundle = bundle;
		m_Completed = false;
		m_Cancelled = false;
		m_Result = null;
		m_Thread = new FunctionThread(this.getFunction(), params);
	}
	
	private void setResults(Data data){
		this.m_Result = data;
		m_DataManager.addData(m_Result, m_Bundle);
	}
	
	private void setComplete(){
		m_Completed = true;
		if(m_Monitor != null){
			m_Monitor.dispose();
			m_Monitor = null;
		}
	}
	
	private void setCancelled(){
		m_Cancelled = true;
		if(m_Monitor != null){
			m_Monitor.dispose();
			m_Monitor = null;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		//monitor.setProgress(thread.getProgress());
		if(m_Monitor != null){
			m_Monitor.setIndeterminate(m_Thread.isIndeterminate());
			m_Monitor.setValue(m_Thread.getProgress());
			m_Monitor.setMaxValue(m_Thread.getMax());
			m_Monitor.setText(m_Thread.getText());
		}
	}

	@Override
	public void execute() {
		//this.monitor = new ProgressMonitor(null, thread.function.getName(), null, 0, PROGRESS_BOUND);
		m_Monitor = new ProgressWindow(m_Thread.m_Function.getName(), 0, m_Thread.getMax(), m_Thread.isIndeterminate(), new OnPress(){

			@Override
			public void act() {
				m_Thread.cancel(true);
			}
			
		});
		m_Thread.addPropertyChangeListener(this);
		m_Thread.execute();
	}

	@Override
	public boolean isComplete() {
		return m_Completed;
	}

	@Override
	public boolean isCancelled() {
		return m_Cancelled;
	}

	@Override
	public void cancel() {
		if(m_Thread!=null){
			m_Thread.cancel(true);
		}
	}

	@Override
	public Data getResults() {
		return m_Result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
