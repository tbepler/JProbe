package plugins.functions.gui;

import java.util.List;

import javax.swing.SwingWorker;

import jprobe.services.Data;
import jprobe.services.DataField;
import jprobe.services.Function;
import jprobe.services.JProbeCore;

public class FunctionExecutor extends SwingWorker{
	
	private Function function;
	private JProbeCore core;
	
	public FunctionExecutor(Function function, JProbeCore core){
		
	}

	@Override
	protected Object doInBackground() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getFunctionName(){
		return function.getName();
	}
	
	public String getFunctionDescription(){
		return function.getDescription();
	}

	public List<Class<? extends Data>> getRequiredDataArgs(){
		return function.getRequiredDataArgs();
	}
	
	public List<Class<? extends Data>> getOptionalDataArgs(){
		return function.getOptionalDataArgs();
	}
	
	public List<DataField> getRequiredFields(){
		return function.getRequiredFields();
	}
	
	public List<DataField> getOptionalFields(){
		return function.getOptionalFields();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
