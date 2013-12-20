package plugins.dataviewer.gui.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import jprobe.services.DataField;

class DataRenderer extends DefaultTableCellRenderer{
	private static final long serialVersionUID = 1L;
	
	
	public DataRenderer(){
		super();
		
	}
	
	@Override
	public void setValue(Object value){
		if(value instanceof DataField){
			DataField field = (DataField) value;
			this.setText((field == null) ? "" : field.asString());
		}
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
		if(value instanceof DataField){
			DataField field = (DataField) value;
			this.setToolTipText((field == null)?"":field.getTooltip());
			//return super.getTableCellRendererComponent(table, field.asString(), isSelected, hasFocus, row, column);
		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
	
}
