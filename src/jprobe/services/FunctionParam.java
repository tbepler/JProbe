package jprobe.services;

import java.util.List;

public class FunctionParam {
	
	private List<Class<? extends Data>> requiredData;
	private List<Class<? extends Data>>	optionalData;
	private List<DataField> requiredFields;
	private List<DataField> optionalFields;
	
	public FunctionParam(List<Class<? extends Data>> requiredData, List<Class<? extends Data>> optionalData,
			List<DataField> requiredFields, List<DataField> optionalFields){
		this.requiredData = requiredData;
		this.optionalData = optionalData;
		this.requiredFields = requiredFields;
		this.optionalFields = optionalFields;
	}
	
	public List<Class<? extends Data>> getRequiredData(){
		return requiredData;
	}
	
	public List<Class<? extends Data>> getOptionalData(){
		return optionalData;
	}
	
	public List<DataField> getRequiredFields(){
		return requiredFields;
	}
	
	public List<DataField> getOptionalFields(){
		return optionalFields;
	}
}
