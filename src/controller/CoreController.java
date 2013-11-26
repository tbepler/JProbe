package controller;

import datatypes.DataType;

public interface CoreController {
	
	public DataType[] selectArgs(Class<? extends DataType>[] types);
	public void update(int event);
	
}
