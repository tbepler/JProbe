package jprobe.services.data;

import java.io.Serializable;



public interface Data extends Serializable{
	
	public void addDataListener(DataListener listener);
	public void removeDataListener(DataListener listener);
	
	public boolean isModifiable(int row, int col);
	
	public String[] getHeaders();
	public Field[][] toTable();
	public boolean setValue(int row, int col, Field value);
	public Field getValue(int row, int col);
	public int getNumRows();
	public int getNumCols();
	
	public String getTooltip();
	

}
