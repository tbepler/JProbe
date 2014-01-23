package plugins.functions.gui.dialog.field.editor.model;

import javax.swing.AbstractSpinnerModel;

import plugins.functions.gui.Activator;
import jprobe.services.ErrorHandler;
import jprobe.services.data.IntegerField;

public class IntFieldModel extends AbstractSpinnerModel{
	private static final long serialVersionUID = 1L;
	
	
	private IntegerField m_Field;
	
	public IntFieldModel(IntegerField field){
		super();
		m_Field = field;
	}
	
	@Override
	public Object getNextValue() {
		if(m_Field.getValue()+m_Field.getIncrement() < m_Field.getMax()){
			try {
				return m_Field.parseInt(m_Field.getValue()+m_Field.getIncrement());
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, Activator.getBundle());
			}
		}
		try {
			return m_Field.parseInt(m_Field.getMax());
		} catch (Exception e) {
			ErrorHandler.getInstance().handleException(e, Activator.getBundle());
		}
		return m_Field;
	}

	@Override
	public Object getPreviousValue() {
		if(m_Field.getValue()-m_Field.getIncrement() > m_Field.getMin()){
			try {
				return m_Field.parseInt(m_Field.getValue()-m_Field.getIncrement());
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, Activator.getBundle());
			}
		}
		try {
			return m_Field.parseInt(m_Field.getMin());
		} catch (Exception e) {
			ErrorHandler.getInstance().handleException(e, Activator.getBundle());
		}
		return m_Field;
	}

	@Override
	public Object getValue() {
		return m_Field;
	}

	@Override
	public void setValue(Object value) {
		if(value == null){
			//do nothing if null
			return;
		}
		if(value.getClass() == m_Field.getClass()){
			m_Field = (IntegerField) value;
			this.fireStateChanged();
			return;
		}
		if(value instanceof String){
			String s = (String) value;
			try {
				m_Field = (IntegerField) m_Field.parseString(s);
				this.fireStateChanged();
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, Activator.getBundle());
			}
			return;
		}
		if(value instanceof Integer){
			try {
				m_Field = m_Field.parseInt((Integer) value);
				this.fireStateChanged();
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, Activator.getBundle());
			} 
		}
	}

}
