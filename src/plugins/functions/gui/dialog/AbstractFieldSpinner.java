package plugins.functions.gui.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.text.DefaultFormatter;

import jprobe.services.data.Field;
import plugins.functions.gui.utils.StateListener;

public abstract class AbstractFieldSpinner extends JSpinner implements FieldEditor{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension PREF_SIZE = new Dimension(50, 10);
	private static final int NUM_COLS = 10;
	
	private Collection<StateListener> m_Listeners = new HashSet<StateListener>();
	
	public AbstractFieldSpinner(){
		super();
		this.init();
	}
	
	public AbstractFieldSpinner(SpinnerModel model) {
		super(model);
		this.init();
	}
	
	private void init(){
		try{
			((JSpinner.DefaultEditor)this.getEditor()).getTextField().setHorizontalAlignment(JTextField.RIGHT);
			((JSpinner.DefaultEditor)this.getEditor()).getTextField().setColumns(NUM_COLS);
			((DefaultFormatter)((JSpinner.DefaultEditor)this.getEditor()).getTextField().getFormatter()).setCommitsOnValidEdit(true);
		} catch (Exception e){
			//oh well
		}
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
