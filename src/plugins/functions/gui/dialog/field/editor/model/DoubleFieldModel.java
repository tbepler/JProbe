package plugins.functions.gui.dialog.field.editor.model;

import javax.swing.AbstractSpinnerModel;

import plugins.functions.gui.Activator;
import jprobe.services.ErrorHandler;
import jprobe.services.data.DoubleField;

public class DoubleFieldModel extends AbstractSpinnerModel{
	private static final long serialVersionUID = 1L;

	private DoubleField m_Field;
	
	public DoubleFieldModel(DoubleField field){
		super();
		m_Field = field;
	}
	
	@Override
	public Object getNextValue() {
		if(m_Field.getValue()+m_Field.getIncrement()<m_Field.getMax()){
			try {
				return m_Field.parseDouble(m_Field.getValue()+m_Field.getIncrement());
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, Activator.getBundle());
			}
		}
		try {
			return m_Field.parseDouble(m_Field.getMax());
		} catch (Exception e) {
			ErrorHandler.getInstance().handleException(e, Activator.getBundle());
		}
		return m_Field;
	}

	@Override
	public Object getPreviousValue() {
		if(m_Field.getValue()-m_Field.getIncrement()>m_Field.getMin()){
			try {
				return m_Field.parseDouble(m_Field.getValue()-m_Field.getIncrement());
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, Activator.getBundle());
			}
		}
		try {
			return m_Field.parseDouble(m_Field.getMin());
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
			m_Field = (DoubleField) value;
			this.fireStateChanged();
			return;
		}
		if(value instanceof String){
			try {
				m_Field = (DoubleField) m_Field.parseString((String) value);
				this.fireStateChanged();
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, Activator.getBundle());
			}
			return;
		}
		if(value instanceof Double){
			try {
				m_Field = m_Field.parseDouble((Double) value);
				this.fireStateChanged();
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, Activator.getBundle());
			}
			return;
		}
	}

}
