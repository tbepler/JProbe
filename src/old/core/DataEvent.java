package old.core;

import old.datatypes.DataType;

public class DataEvent extends CoreEvent{
	
	public static final int EVENT_DATA_ADDED = 1;
	public static final int EVENT_DATA_REMOVED = 2;
	public static final int EVENT_DATA_CLEARED = 3;
	
	public final DataType data;
	public final int event;
	
	public DataEvent(DataType data, int eventCode){
		this.data = data;
		this.event = eventCode;
	}
	
	
}
