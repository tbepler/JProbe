package plugins.functions.gui.dialog.field.editor;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jprobe.services.data.DoubleField;
import jprobe.services.data.Field;
import jprobe.services.function.FieldParameter;

public class DoubleFieldSpinner extends AbstractFieldSpinner implements ChangeListener{
	private static final long serialVersionUID = 1L;
	
	private DoubleField m_Value;
	
	public DoubleFieldSpinner(FieldParameter fieldParam, DoubleField doubleField){
		super(fieldParam, doubleField);
		this.setValue(doubleField);
		m_Value = doubleField;
		this.addChangeListener(this);
		this.setToolTipText(m_Value.getTooltip());
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
			m_Value = field;
		}
	}

}
