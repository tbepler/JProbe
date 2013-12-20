package plugins.dataviewer.gui.table;

import javax.swing.table.AbstractTableModel;

import jprobe.services.Data;
import jprobe.services.DataEvent;
import jprobe.services.DataField;
import jprobe.services.DataListener;

public class DataTableModel extends AbstractTableModel implements DataListener{
	private static final long serialVersionUID = 1L;
	
	
	private Data data;
	
	public DataTableModel(Data data){
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
		return data.getValue(rowIndex, columnIndex);
	}
	
	@Override
	public Class getColumnClass(int c){
		return DataField.class;
	}
	
	@Override
	public boolean isCellEditable(int row, int col){
		return data.isModifiable(row, col);
	}
	
	@Override
	public void setValueAt(Object value, int row, int col){
		if(value instanceof DataField){
			DataField field = (DataField) value;
			data.setValue(row, col, field);
			fireTableCellUpdated(row, col);
			return;
		}
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

	@Override
	public void update(DataEvent event) {
		switch(event.getType()){
		
		case FIELD_UPDATED:
			if(event.getRow() >= 0 && event.getColumn() >= 0){
				this.fireTableCellUpdated(event.getRow(), event.getColumn());
			}else{
				this.fireTableDataChanged();
			}
			break;
		
		case ROW_INSERTED:
			if(event.getRow() >= 0){
				this.fireTableRowsInserted(event.getRow(), event.getRow());
			}else{
				this.fireTableRowsInserted(0, data.getNumRows()-1);
			}
			break;
		
		case ROW_DELETED:
			if(event.getRow() >= 0){
				this.fireTableRowsDeleted(event.getRow(), event.getRow());
			}else{
				this.fireTableRowsDeleted(0, data.getNumRows()-1);
			}
			break;
			
		default:
			this.fireTableStructureChanged();
			break;
			
		}
	}
	
}
