package plugins.dataviewer.gui.table;

import java.awt.Component;
import java.text.ParseException;

import javax.swing.AbstractCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

import plugins.testDataAndFunction.DecimalField;
import jprobe.services.DataField;
import jprobe.services.IntegerField;

public class DataCellEditor extends AbstractCellEditor implements TableCellEditor{
	
	private JSpinner spinner = null;
	private JFormattedTextField field = null;
	private DataField value = null;
	
	public DataCellEditor(){
		super();
	}
	
	@Override
	public Object getCellEditorValue() {
		return value;
	}
	
	private boolean stopIntEditing(){
		IntegerField val = (IntegerField) value;
		int i = (Integer) spinner.getValue();
		if(val.isValid(i)){
			value = val.parseInt(i);
			return true;
		}
		return false;
	}
	
	private boolean stopDoubleEditing(){
		DecimalField val = (DecimalField) value;
		double d = (Double) spinner.getValue();
		if(val.isValid(d)){
			value = val.parseDouble(d);
			return true;
		}
		return false;
	}
	
	private boolean stopTextEditing(){
		try {
			field.commitEdit();
		} catch (ParseException e) {
			return false;
		}
		value = (DataField) field.getValue();
		return true;
	}
	
	@Override
	public boolean stopCellEditing(){
		if(value instanceof IntegerField){
			return stopIntEditing();
		}
		if(value instanceof DecimalField){
			return stopDoubleEditing();
		}
		return stopTextEditing();
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
		if(value instanceof DataField){
			this.value = (DataField) value;
			if(value instanceof IntegerField){
				IntegerField i = (IntegerField) value;
				spinner = new JSpinner(new SpinnerNumberModel(i.getValue(), i.getMin(), i.getMax(), i.getIncrement()));
				return spinner;
			}
			if(value instanceof DecimalField){
				DecimalField d = (DecimalField) value;
				spinner = new JSpinner(new SpinnerNumberModel(d.getValue(), d.getMin(), d.getMax(), d.getIncrement()));
				return spinner;
			}
			field = new JFormattedTextField(new DataFieldFormatter(this.value));
			field.setValue(this.value);
			return field;
		}
		return null;
	}

}
