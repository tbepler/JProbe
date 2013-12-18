package jprobe.services;

import java.io.Serializable;


public interface Data extends Serializable{
	
	public void addDataListener(DataListener listener);
	public void removeDataListener(DataListener listener);
	
	public boolean isModifiable();
	
	public DataField[][] toTable();
	public boolean setValue(int row, int col, DataField value);
	public DataField getValue(int row, int col);
	public int getNumRows();
	public int getNumCols();
	
	public String getTooltip();
	

}
