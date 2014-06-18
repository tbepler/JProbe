package jprobe.services.function;

import java.awt.Dimension;
import java.text.ParseException;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.progress.ProgressListener;

public abstract class SpinnerArgument<P,T> extends AbstractArgument<P> implements ChangeListener{
	
	public static interface Spinner<V>{
		public V next(V cur);
		public V prev(V cur);
	}
	
	private class Model<V> extends AbstractSpinnerModel{
		private static final long serialVersionUID = 1L;
		
		private final Spinner<V> m_Spinner;
		private V m_Val;
		
		private Model(Spinner<V> spin, V startValue){
			m_Spinner = spin;
		}
		
		@Override
		public V getNextValue() {
			return m_Spinner.next(m_Val);
		}

		@Override
		public V getPreviousValue() {
			return m_Spinner.prev(m_Val);
		}

		@Override
		public V getValue() {
			return m_Val;
		}

		@Override
		public void setValue(Object value) {
			try{
				@SuppressWarnings("unchecked")
				V val = (V) value;
				m_Val = val;
				this.fireStateChanged();
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	public static JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
             return ((JSpinner.DefaultEditor)editor).getTextField();
        } else {
             throw new RuntimeException("Unexpected editor type: " + spinner.getEditor().getClass() + " isn't a descendant of DefaultEditor");
        }
   }
	
	public static final String PROTOTYPE_TEXT = "entry";
	
	private final JSpinner m_Editor;
	private final Model<T> m_Model;
	
	protected SpinnerArgument(String name, String tooltip, String category, Character shortFlag, String prototypeVal, boolean optional, T startValue, Spinner<T> model, int textAlignment) {
		super(name, tooltip, category, shortFlag, prototypeVal, optional);
		m_Model = new Model<T>(model, startValue);
		m_Model.addChangeListener(this);
		m_Editor = new JSpinner(m_Model);
		getTextField(m_Editor).setHorizontalAlignment(textAlignment);
		getTextField(m_Editor).setEditable(true);
		getTextField(m_Editor).setText(PROTOTYPE_TEXT);
		Dimension size = getTextField(m_Editor).getPreferredSize();
		m_Model.setValue(startValue);
		getTextField(m_Editor).setPreferredSize(size);
		getTextField(m_Editor).setMinimumSize(size);
	}
	
	protected abstract boolean isValid(T value);
	protected abstract void process(P params, T value);

	@Override
	public void parse(ProgressListener l, P params, String[] args){
		if(args.length < 1 || args.length > 1){
			throw new RuntimeException(this.getName() + " requires 1 argument. Received "+args.length);
		}
		getTextField(m_Editor).setText(args[0]);
		try {
			getTextField(m_Editor).commitEdit();
		} catch (ParseException e) {
			throw new RuntimeException(this.getName() + " unable to parse argument \""+args[0]+"\"");
		}
		T val = m_Model.getValue();
		if(!isValid(val)){
			throw new RuntimeException(this.getName() + " argument \""+args[0]+"\" is not valid");
		}
		process(params, val);
	}
	
	@Override
	public boolean isValid() {
		return this.isValid(m_Model.getValue());
	}

	@Override
	public JComponent getComponent() {
		return m_Editor;
	}

	@Override
	public void process(P params) {
		this.process(params, m_Model.getValue());
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == m_Model){
			this.notifyListeners();
		}
	}

}
