package plugins.dataviewer.gui.table;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import jprobe.services.Data;
import jprobe.services.DataField;

public class DataTab extends JTable{
	private static final long serialVersionUID = 1L;


	
	public DataTab(Data data){
		this.setDefaultRenderer(DataField.class, new DataRenderer());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
