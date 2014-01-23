package plugins.functions.gui.dialog.field.editor;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jprobe.services.data.Field;
import jprobe.services.data.IntegerField;
import jprobe.services.function.FieldParameter;

public class IntegerFieldSpinner extends AbstractFieldSpinner implements ChangeListener{
	private static final long serialVersionUID = 1L;
	
	private IntegerField m_Value;
	
	public IntegerFieldSpinner(FieldParameter fieldParam, IntegerField intField){
		super(fieldParam, intField);
		this.getModel().setValue(intField);
		m_Value = intField;
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
		if(o instanceof IntegerField){
			IntegerField field = (IntegerField) o;
			m_Value = field;
		}
	}

}
