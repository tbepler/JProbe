package plugins.functions.gui.dialog;

import java.awt.Component;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

import jprobe.services.data.Field;
import plugins.functions.gui.utils.StateListener;

public abstract class AbstractFieldSpinner extends JSpinner implements FieldEditor{
	private static final long serialVersionUID = 1L;
	
	private Collection<StateListener> m_Listeners = new HashSet<StateListener>();
	
	public AbstractFieldSpinner(){
		super();
	}
	
	public AbstractFieldSpinner(SpinnerModel model) {
		super(model);
	}

	protected abstract Field getFieldValue();
	
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
