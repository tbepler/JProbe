package plugins.functions.gui.dialog.field.editor;

import java.awt.Component;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

import jprobe.services.data.DoubleField;
import jprobe.services.data.Field;
import jprobe.services.data.IntegerField;
import plugins.functions.gui.dialog.field.editor.model.FieldSpinnerModel;
import plugins.functions.gui.utils.StateListener;

public abstract class AbstractFieldSpinner extends JSpinner implements FieldEditor{
	private static final long serialVersionUID = 1L;
	
	private Collection<StateListener> m_Listeners = new HashSet<StateListener>();
	
	protected AbstractFieldSpinner(IntegerField template){
		super(new FieldSpinnerModel(template));
		//this.setEditor(new FieldSpinnerEditor(this, template));
	}
	
	protected AbstractFieldSpinner(DoubleField template){
		super(new FieldSpinnerModel(template));
		//this.setEditor(new FieldSpinnerEditor(this, template));
	}

	protected abstract Field getFieldValue();
	
	@Override
	protected JComponent createEditor(SpinnerModel model){
		if(model instanceof FieldSpinnerModel){
			FieldSpinnerModel m = (FieldSpinnerModel) model;
			return new FieldSpinnerEditor(this, m.getTemplate());
		}else{
			return super.createEditor(model);
		}
	}
	
	@Override
	public Field getValue(){
		return this.getFieldValue();
	}
	
	protected void notifyListeners(){
		for(StateListener l : m_Listeners){
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
	public Component getEditorComponent() {
		return this;
	}

}
