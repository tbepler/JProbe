package plugins.dataviewer.gui.services;

import jprobe.services.data.Data;

public interface DataViewer {
	
	public void selectData(Data data);
	public void displayData(Data data);
	public void closeData(Data data);
	
	
}
