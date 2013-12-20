package plugins.dataviewer.gui.table;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import jprobe.services.Data;
import jprobe.services.DataField;

public class DataTable extends JTable{
	private static final long serialVersionUID = 1L;
	
	public DataTable(Data data){
		super(new DataTableModel(data));
		this.setDefaultRenderer(DataField.class, new DataRenderer());
		this.setDefaultEditor(DataField.class, new DataCellEditor());
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
