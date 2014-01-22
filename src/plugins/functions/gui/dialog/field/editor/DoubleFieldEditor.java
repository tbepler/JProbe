package plugins.functions.gui.dialog.field.editor;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jprobe.services.data.DoubleField;
import jprobe.services.data.Field;
import jprobe.services.function.FieldParameter;

public class DoubleFieldEditor extends AbstractFieldSpinner implements ChangeListener{
	private static final long serialVersionUID = 1L;
	
	private DoubleField m_Value;
	private FieldParameter m_FieldParam;
	private boolean m_Valid;
	
	public DoubleFieldEditor(FieldParameter fieldParam, DoubleField doubleField){
		super(doubleField);
		this.getModel().setValue(doubleField);
		m_Value = doubleField;
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
		if(o instanceof DoubleField){
			DoubleField field = (DoubleField) o;
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
