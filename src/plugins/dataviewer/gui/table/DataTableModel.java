package plugins.dataviewer.gui.table;

import javax.swing.table.AbstractTableModel;

import plugins.dataviewer.gui.Activator;
import jprobe.services.ErrorHandler;
import jprobe.services.data.Data;
import jprobe.services.data.DataEvent;
import jprobe.services.data.Field;
import jprobe.services.data.DataListener;

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
		return Field.class;
	}
	
	@Override
	public boolean isCellEditable(int row, int col){
		return data.isModifiable(row, col);
	}
	
	@Override
	public void setValueAt(Object value, int row, int col){
		if(value instanceof Field){
			Field field = (Field) value;
			data.setValue(row, col, field);
			fireTableCellUpdated(row, col);
			return;
		}
		if(value instanceof String){
			String val = (String) value;
			Field field = data.getValue(row, col);
			if(field.isValid(val)){
				try{
					Field newField = field.parseString(val);
					if(data.setValue(row, col, newField)){
						fireTableCellUpdated(row, col);
					}
				} catch (Exception e){
					ErrorHandler.getInstance().handleException(e, Activator.BUNDLE);
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
