package jprobe.services;

import java.util.List;


public interface FunctionExecutor {

	public void execute(FunctionParam params);

	public Data getResult();

	public boolean isDone();

	public boolean isCancelled();

	public int getProgress();

	public String getFunctionName();

	public String getFunctionDescription();

	public List<Class<? extends Data>> getRequiredDataArgs();

	public List<Class<? extends Data>> getOptionalDataArgs();

	public List<DataField> getRequiredFields();

	public List<DataField> getOptionalFields();

}