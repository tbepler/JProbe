package plugins.functions.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import jprobe.services.Data;
import jprobe.services.DataField;
import jprobe.services.Function;
import jprobe.services.FunctionEvent;
import jprobe.services.FunctionExecutor;
import jprobe.services.FunctionParam;
import jprobe.services.JProbeCore;
import jprobe.services.FunctionListener;

public class SwingFunctionExecutor implements PropertyChangeListener, FunctionExecutor{
	
	private Function function;
	private FunctionThread thread;
	private JProbeCore core;
	private boolean done;
	
	public SwingFunctionExecutor(Function function, JProbeCore core){
		this.function = function;
		this.core = core;
		this.thread = null;
		this.done = false;
	}
	
	/* (non-Javadoc)
	 * @see plugins.functions.gui.FunctionExecutorI#execute(jprobe.services.FunctionParam)
	 */
	@Override
	public void execute(FunctionParam params){
		thread = new FunctionThread(function, params);
		thread.addPropertyChangeListener(this);
		thread.execute();	
	}
	
	/* (non-Javadoc)
	 * @see plugins.functions.gui.FunctionExecutorI#getResult()
	 */
	@Override
	public Data getResult(){
		if(this.isDone()){
			try {
				return (Data) thread.get();
			} catch (Exception e){
				//proceed
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see plugins.functions.gui.FunctionExecutorI#isDone()
	 */
	@Override
	public boolean isDone(){
		return done;
	}
	
	/* (non-Javadoc)
	 * @see plugins.functions.gui.FunctionExecutorI#isCancelled()
	 */
	@Override
	public boolean isCancelled(){
		if(thread != null){
			return thread.isCancelled();
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see plugins.functions.gui.FunctionExecutorI#getProgress()
	 */
	@Override
	public int getProgress(){
		if(thread != null){
			return thread.getProgress();
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see plugins.functions.gui.FunctionExecutorI#getFunctionName()
	 */
	@Override
	public String getFunctionName(){
		return function.getName();
	}
	
	/* (non-Javadoc)
	 * @see plugins.functions.gui.FunctionExecutorI#getFunctionDescription()
	 */
	@Override
	public String getFunctionDescription(){
		return function.getDescription();
	}

	/* (non-Javadoc)
	 * @see plugins.functions.gui.FunctionExecutorI#getRequiredDataArgs()
	 */
	@Override
	public List<Class<? extends Data>> getRequiredDataArgs(){
		return function.getRequiredDataArgs();
	}
	
	/* (non-Javadoc)
	 * @see plugins.functions.gui.FunctionExecutorI#getOptionalDataArgs()
	 */
	@Override
	public List<Class<? extends Data>> getOptionalDataArgs(){
		return function.getOptionalDataArgs();
	}
	
	/* (non-Javadoc)
	 * @see plugins.functions.gui.FunctionExecutorI#getRequiredFields()
	 */
	@Override
	public List<DataField> getRequiredFields(){
		return function.getRequiredFields();
	}
	
	/* (non-Javadoc)
	 * @see plugins.functions.gui.FunctionExecutorI#getOptionalFields()
	 */
	@Override
	public List<DataField> getOptionalFields(){
		return function.getOptionalFields();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
