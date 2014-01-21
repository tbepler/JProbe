package plugins.functions.gui.dialog;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plugins.functions.gui.Activator;
import jprobe.services.ErrorHandler;
import jprobe.services.data.DoubleField;
import jprobe.services.data.Field;
import jprobe.services.function.FieldParameter;

public class DoubleFieldEditor extends AbstractFieldSpinner implements ChangeListener{
	private static final long serialVersionUID = 1L;
	
	private DoubleField m_Value;
	private FieldParameter m_FieldParam;
	private boolean m_Valid;
	
	public DoubleFieldEditor(FieldParameter fieldParam, DoubleField doubleField){
		super(new SpinnerNumberModel(doubleField.getValue(), doubleField.getMin(), doubleField.getMax(), doubleField.getIncrement()));
		m_Value = doubleField;
		m_FieldParam = fieldParam;
		m_Valid = m_FieldParam.isValid(m_Value);
		this.addChangeListener(this);
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
		double num = (Double) this.getModel().getValue();
		if(m_Value.isValid(num)){
			boolean newState = m_Valid;
			try {
				m_Value = m_Value.parseDouble(num);
				newState = m_FieldParam.isValid(m_Value);
			} catch (Exception e) {
				newState = false;
				ErrorHandler.getInstance().handleException(e, Activator.getBundle());
			}
			if(newState != m_Valid){
				m_Valid = newState;
				this.notifyListeners();
			}
		}
	}

}
