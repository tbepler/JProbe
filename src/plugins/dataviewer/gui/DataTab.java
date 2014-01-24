package plugins.dataviewer.gui;

import javax.swing.JScrollPane;

import plugins.dataviewer.gui.table.DataTable;
import jprobe.services.data.Data;

public class DataTab extends JScrollPane{
	private static final long serialVersionUID = 1L;
	
	private Data m_Data;
	
	public DataTab(Data data){
		super(new DataTable(data));
		m_Data = data;
	}
	
	public Data getData(){
		return m_Data;
	}
	
}
