package plugins.functions.gui.dialog.field.editor;

import java.awt.Color;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;

import plugins.functions.gui.Constants;
import plugins.functions.gui.utils.StateListener;
import plugins.functions.gui.utils.ValidStateNotifier;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JSpinner;

import jprobe.services.data.Field;
import jprobe.services.function.FieldParameter;

public class FieldSpinnerEditor extends JSpinner.DefaultEditor implements ValidStateNotifier{
	private static final long serialVersionUID = 1L;
	
	public class FieldFormatterFactory extends JFormattedTextField.AbstractFormatterFactory{

		private FieldFormatter m_Formatter;
		
		public FieldFormatterFactory(){
			m_Formatter = new FieldFormatter();
		}
		
		@Override
		public AbstractFormatter getFormatter(JFormattedTextField tf) {
			return m_Formatter;
		}
		
	}

	public class FieldFormatter extends JFormattedTextField.AbstractFormatter{
		private static final long serialVersionUID = 1L;
		
		public FieldFormatter(){
			super();
		}
		
		@Override
		public Object stringToValue(String text) throws ParseException {
			try {
				Field entered = m_Template.parseString(text);
				setValid(m_Param.isValid(entered));
				return entered;
			} catch (Exception e) {
				setValid(false);
				throw new ParseException("Unable to parse \""+text+"\" to "+m_Template.getClass(), 0);
			}
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if(value instanceof Field){
				Field f = (Field) value;
				try {
					setValid(m_Param.isValid(m_Template.parseString(f.asString())));
				} catch (Exception e) {
					setValid(false);
				}
				return f.asString();
			}
			throw new ParseException("Unable to parse object "+value, 0);
		}
		
	}
	
	private static final int MIN_COLS = Constants.SPINNER_MIN_COLS;
	private static final int MAX_COLS = Constants.SPINNER_MAX_COLS;
	
	private Field m_Template;
	private FieldParameter m_Param;
	private boolean m_Valid;
	private Collection<StateListener> m_Listeners = new HashSet<StateListener>();

	public FieldSpinnerEditor(JSpinner spinner, FieldParameter fieldParam) {
		super(spinner);
		m_Param = fieldParam;
		m_Template = m_Param.getType();
		m_Valid = m_Param.isValid(m_Template);
		this.getTextField().setFormatterFactory(new FieldFormatterFactory());
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
	
	private void setValid(boolean valid){
		if(m_Valid != valid){
			m_Valid = valid;
			this.notifyListeners();
		}
	}
	
	protected void notifyListeners(){
		for(StateListener l : new HashSet<StateListener>(m_Listeners)){
			l.update(this);
		}
	}

	@Override
	public void addStateListener(StateListener l) {
		m_Listeners.add(l);
	}

	@Override
	public void removeStateListener(StateListener l) {
		m_Listeners.remove(l);
	}

	@Override
	public boolean isStateValid() {
		return m_Valid;
	}

}
