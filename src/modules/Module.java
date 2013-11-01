package modules;

import datatypes.DataType;
import datatypes.Param;

public interface Module <D extends DataType> {
	
	
	public D run(Param p);
	
	
	
}
