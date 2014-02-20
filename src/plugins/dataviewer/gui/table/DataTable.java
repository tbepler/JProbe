package plugins.dataviewer.gui.table;

import javax.swing.JTable;

import jprobe.services.data.Data;
import jprobe.services.data.Field;

public class DataTable extends JTable{
	private static final long serialVersionUID = 1L;

	public DataTable(Data data){
		super(new DataTableModel(data));
		this.setDefaultRenderer(Field.class, new DataRenderer());
		this.setDefaultEditor(Field.class, new DataCellEditor());
		//TableFormatter.format(this, TableFormatter.Method.MODE);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}









	
	
	
	
	
	
	
	
	
	
	
	
}
