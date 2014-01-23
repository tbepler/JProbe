package plugins.functions.gui.dialog.field.editor;

import java.awt.Color;
import java.text.ParseException;

import plugins.functions.gui.Constants;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JSpinner;

import jprobe.services.data.Field;

public class FieldSpinnerEditor extends JSpinner.DefaultEditor{
	private static final long serialVersionUID = 1L;
	
	public static class FieldFormatterFactory extends JFormattedTextField.AbstractFormatterFactory{

		private FieldFormatter m_Formatter;
		
		public FieldFormatterFactory(Field template){
			m_Formatter = new FieldFormatter(template);
		}
		
		@Override
		public AbstractFormatter getFormatter(JFormattedTextField tf) {
			return m_Formatter;
		}
		
	}

	public static class FieldFormatter extends JFormattedTextField.AbstractFormatter{
		private static final long serialVersionUID = 1L;
		
		private Field m_Field;
		
		public FieldFormatter(Field f){
			m_Field = f;
		}
		
		@Override
		public Object stringToValue(String text) throws ParseException {
			try {
				return m_Field.parseString(text);
			} catch (Exception e) {
				throw new ParseException("Unable to parse \""+text+"\" to "+m_Field.getClass(), 0);
			}
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if(value == null){
				return "";
			}
			if(value instanceof Field){
				Field f = (Field) value;
				return f.asString();
			}
			throw new ParseException("Unable to parse class "+value.getClass(), 0);
		}
		
	}
	
	private static final int MIN_COLS = Constants.SPINNER_MIN_COLS;
	private static final int MAX_COLS = Constants.SPINNER_MAX_COLS;

	public FieldSpinnerEditor(JSpinner spinner, Field template) {
		super(spinner);
		this.getTextField().setFormatterFactory(new FieldFormatterFactory(template));
		this.getTextField().setHorizontalAlignment(JTextField.RIGHT);
		if(this.getTextField().getColumns() < MIN_COLS){
			this.getTextField().setColumns(MIN_COLS);
		}
		if(this.getTextField().getColumns() > MAX_COLS){
			this.getTextField().setColumns(MAX_COLS);
		}
		this.getTextField().setValue(spinner.getValue());
		this.getTextField().setBackground(Color.white);
		this.getTextField().setEditable(true);
	}

}
