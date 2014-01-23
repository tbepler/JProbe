package plugins.functions.gui.dialog.field.editor;

import java.awt.Component;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JSpinner;

import jprobe.services.data.DoubleField;
import jprobe.services.data.Field;
import jprobe.services.data.IntegerField;
import jprobe.services.function.FieldParameter;
import plugins.functions.gui.dialog.field.editor.model.FieldSpinnerModel;
import plugins.functions.gui.utils.StateListener;
import plugins.functions.gui.utils.StateNotifier;
import plugins.functions.gui.utils.ValidStateNotifier;

public abstract class AbstractFieldSpinner extends JSpinner implements FieldEditor, StateListener{
	private static final long serialVersionUID = 1L;
	
	private FieldParameter m_Param;
	private boolean m_Valid;
	private Collection<StateListener> m_Listeners = new HashSet<StateListener>();
	
	protected AbstractFieldSpinner(FieldParameter param, IntegerField template){
		super(new FieldSpinnerModel(template));
		this.init(param, template);
		//this.setEditor(new FieldSpinnerEditor(this, template));
	}
	
	protected AbstractFieldSpinner(FieldParameter param, DoubleField template){
		super(new FieldSpinnerModel(template));
		this.init(param, template);
		//this.setEditor(new FieldSpinnerEditor(this, template));
	}
	
	private void init(FieldParameter param, Field template){
		m_Param = param;
		m_Valid = param.isValid(template);
		FieldSpinnerEditor editor = new FieldSpinnerEditor(this, m_Param);
		editor.addStateListener(this);
		this.setEditor(editor);
		this.setValue(template);
	}

	protected abstract Field getFieldValue();
	
	@Override
	public boolean isStateValid(){
		return m_Valid;
	}
	
	@Override
	public void update(StateNotifier notifier){
		if(notifier instanceof ValidStateNotifier){
			ValidStateNotifier observed = (ValidStateNotifier) notifier;
			boolean newState = observed.isStateValid();
			if(newState != m_Valid){
				m_Valid = newState;
				this.notifyListeners();
			}
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
