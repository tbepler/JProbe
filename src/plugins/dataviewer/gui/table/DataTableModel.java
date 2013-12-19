package plugins.dataviewer.gui.table;

import javax.swing.table.AbstractTableModel;

import jprobe.services.Data;
import jprobe.services.DataField;

class DataTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	
	
	private Data data;
	
	DataTableModel(Data data){
		this.data = data;
	}
	
	@Override
	public int getColumnCount() {
		return data.getNumCols();
	}

	@Override
	public int getRowCount() {
		return data.getNumRows();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Class getColumnClass(int c){
		return data.getValue(0, c).getClass();
	}
	
	@Override
	public boolean isCellEditable(int row, int col){
		return data.isModifiable(row, col);
	}
	
	@Override
	public void setValueAt(Object value, int row, int col){
		if(value instanceof String){
			String val = (String) value;
			DataField field = data.getValue(row, col);
			if(field.isValid(val)){
				DataField newField = field.parseString(val);
				if(data.setValue(row, col, newField)){
					fireTableCellUpdated(row, col);
				}
			}
		}
	}
	
}
