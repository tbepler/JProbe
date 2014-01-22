package plugins.functions.gui.dialog.field.editor.model;

import javax.swing.AbstractSpinnerModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jprobe.services.data.DoubleField;
import jprobe.services.data.IntegerField;

public class FieldSpinnerModel extends AbstractSpinnerModel implements ChangeListener{
	private static final long serialVersionUID = 1L;
	
	private SpinnerModel m_Model;
	
	public FieldSpinnerModel(IntegerField intField){
		m_Model = new IntFieldModel(intField);
		m_Model.addChangeListener(this);
	}
	
	public FieldSpinnerModel(DoubleField doubleField){
		m_Model = new DoubleFieldModel(doubleField);
		m_Model.addChangeListener(this);
	}
	
	@Override
	public Object getNextValue() {
		return m_Model.getNextValue();
	}

	@Override
	public Object getPreviousValue() {
		return m_Model.getPreviousValue();
	}

	@Override
	public Object getValue() {
		return m_Model.getValue();
	}

	@Override
	public void setValue(Object arg0) {
		m_Model.setValue(arg0);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		this.fireStateChanged();
	}

}
