package jprobe.services.function;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	
	private final JSpinner m_Editor;
	private final Model<T> m_Model;
	
	protected SpinnerArgument(String name, String tooltip, String category, boolean optional, T startValue, Spinner<T> model, int textAlignment) {
		super(name, tooltip, category, optional);
		m_Model = new Model<T>(model, startValue);
		m_Model.addChangeListener(this);
		m_Model.setValue(startValue);
		m_Editor = new JSpinner(m_Model);
		getTextField(m_Editor).setHorizontalAlignment(textAlignment);
		getTextField(m_Editor).setEditable(true);
	}
	
	protected abstract boolean isValid(T value);
	protected abstract void process(P params, T value);

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
