package plugins.functions.gui.dialog.field.editor;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jprobe.services.data.Field;
import jprobe.services.data.IntegerField;
import jprobe.services.function.FieldParameter;

public class IntegerFieldSpinner extends AbstractFieldSpinner implements ChangeListener{
	private static final long serialVersionUID = 1L;
	
	private IntegerField m_Value;
	private FieldParameter m_FieldParam;
	private boolean m_Valid;
	
	public IntegerFieldSpinner(FieldParameter fieldParam, IntegerField intField){
		super(intField);
		this.getModel().setValue(intField);
		m_Value = intField;
		m_FieldParam = fieldParam;
		m_Valid = m_FieldParam.isValid(m_Value);
		this.addChangeListener(this);
		this.setToolTipText(m_Value.getTooltip());
	}
	
	@Override
	public boolean isStateValid() {
		return m_Valid;
	}

	@Override
	protected Field getFieldValue() {
		return m_Value;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		Object o = this.getModel().getValue();
		if(o instanceof IntegerField){
			IntegerField field = (IntegerField) o;
			boolean newState = m_FieldParam.isValid(field);
			if(newState){
				m_Value = field;
			}
			if(newState != m_Valid){
				m_Valid = newState;
				this.notifyListeners();
			}
		}
	}

}
