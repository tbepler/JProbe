package old.view.data;

import old.datatypes.DataType;

public interface DataContext {
	
	public DataType getCurrentData();
	
	public void addDataListener(DataListener listener);
	
	public void removeDataListener(DataListener listener);
	
}
