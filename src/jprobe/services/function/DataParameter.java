package jprobe.services.function;

import jprobe.services.data.Data;

public interface DataParameter extends Parameter{
	
	public Class<? extends Data> getType();
	public boolean isValide(Data data);
	public void setValue(Data data) throws InvalidArgumentsException;
	public Data getValue();
	
}
