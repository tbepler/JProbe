package plugins.dataviewer.gui.table;

import javax.swing.JScrollPane;

import jprobe.services.Data;

public class DataTab extends JScrollPane{
	private static final long serialVersionUID = 1L;
	
	
	public DataTab(Data data){
		super(new DataTable(data));
	}
	
	
}
