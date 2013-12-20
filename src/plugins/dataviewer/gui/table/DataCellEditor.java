package plugins.dataviewer.gui.table;

import java.awt.Color;
import java.awt.Component;
import java.text.ParseException;

import javax.swing.AbstractCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.text.DefaultFormatter;

import plugins.testDataAndFunction.DecimalField;
import jprobe.services.DataField;
import jprobe.services.DoubleField;
import jprobe.services.IntegerField;

public class DataCellEditor extends AbstractCellEditor implements TableCellEditor{
	
	private static final Border red = new LineBorder(Color.red);
	private static final Border black = new LineBorder(Color.black);
	
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
		System.out.println("Stop int editing");
		if(!field.isEditValid()){
			spinner.setBorder(red);
			return false;
		}
		try {
			spinner.commitEdit();
		} catch (ParseException e) {
			spinner.setBorder(red);
			return false;
		}
		IntegerField val = (IntegerField) value;
		int i = (Integer) spinner.getValue();
		if(val.isValid(i)){
			value = val.parseInt(i);
			this.fireEditingStopped();
			return true;
		}
		spinner.setBorder(red);
		return false;
	}
	
	private boolean stopDoubleEditing(){
		System.out.println("Stop double editing");
		if(!field.isEditValid()){
			spinner.setBorder(red);
			return false;
		}
		try {
			spinner.commitEdit();
		} catch (ParseException e) {
			spinner.setBorder(red);
			return false;
		}
		DoubleField val = (DoubleField) value;
		double d = (Double) spinner.getValue();
		if(val.isValid(d)){
			value = val.parseDouble(d);
			this.fireEditingStopped();
			return true;
		}
		spinner.setBorder(red);
		return false;
	}
	
	private boolean stopTextEditing(){
		try {
			field.commitEdit();
		} catch (ParseException e) {
			field.setBorder(red);
			return false;
		}
		value = (DataField) field.getValue();
		this.fireEditingStopped();
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
	
	private void initSpinner(JSpinner spinner){
		spinner.setBorder(black);
		field = (JFormattedTextField) spinner.getEditor().getComponent(0);
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
		if(value instanceof DataField){
			this.value = (DataField) value;
			if(this.value == null){
				System.out.println("Value is null.");
				System.out.println("Object passed = "+value);
			}
			if(value instanceof IntegerField){
				IntegerField i = (IntegerField) value;
				spinner = new JSpinner(new SpinnerNumberModel(i.getValue(), i.getMin(), i.getMax(), i.getIncrement()));
				initSpinner(spinner);
				return spinner;
			}
			if(value instanceof DoubleField){
				DoubleField d = (DoubleField) value;
				spinner = new JSpinner(new SpinnerNumberModel(d.getValue(), d.getMin(), d.getMax(), d.getIncrement()));
				initSpinner(spinner);
				return spinner;
			}
			field = new JFormattedTextField(new DataFieldFormatter(this.value));
			field.setValue(this.value);
			field.setBorder(black);
			return field;
		}
		return null;
	}

}
