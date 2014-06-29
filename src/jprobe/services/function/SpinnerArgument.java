package jprobe.services.function;

import java.awt.Dimension;
import javax.swing.AbstractSpinnerModel;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import jprobe.services.Workspace;
import util.gui.ChangeNotifierJSpinner;
import util.progress.ProgressListener;

public abstract class SpinnerArgument<P,T> extends AbstractArgument<P,ChangeNotifierJSpinner>{
	
	protected abstract static class Model<V> extends AbstractSpinnerModel{
		private static final long serialVersionUID = 1L;
		
		private V m_Val;
		
		protected Model(V startValue){
			m_Val = startValue;
		}
		
		@Override
		public abstract V getNextValue();
		@Override
		public abstract V getPreviousValue();

		@Override
		public V getValue() {
			return m_Val;
		}

		@Override
		public void setValue(Object value) {
			try{
				@SuppressWarnings("unchecked")
				V val = (V) value;
				if(val != m_Val){
					m_Val = val;
					this.fireStateChanged();
				}
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
	
	private final int m_TextAlignment;
	
	protected SpinnerArgument(String name, String tooltip, String category, Character shortFlag, String prototypeVal, boolean optional, int textAlignment) {
		super(name, tooltip, category, shortFlag, prototypeVal, optional);
		m_TextAlignment = textAlignment;
	}
	
	protected abstract Model<T> createModel();
	protected abstract T parse(String arg);
	protected abstract boolean isValid(T value);
	protected abstract void process(P params, T value);
	
	
	@Override
	public ChangeNotifierJSpinner createComponent(Workspace w){
		JTextField forSizing = new JTextField(PROTOTYPE_TEXT);
		Dimension size = forSizing.getPreferredSize();
		
		ChangeNotifierJSpinner spinner = new ChangeNotifierJSpinner(this.createModel());
		getTextField(spinner).setHorizontalAlignment(m_TextAlignment);
		getTextField(spinner).setEditable(true);
		getTextField(spinner).setPreferredSize(size);
		getTextField(spinner).setMinimumSize(size);
		
		return spinner;
	}

	@Override
	public void parse(ProgressListener l, P params, String[] args){
		if(args.length < 1 || args.length > 1){
			throw new RuntimeException(this.getName() + " requires 1 argument. Received "+args.length);
		}
		T val = this.parse(args[0]);
		if(!isValid(val)){
			throw new RuntimeException(this.getName() + " argument \""+args[0]+"\" is not valid");
		}
		process(params, val);
	}
	
	@SuppressWarnings("unchecked")
	protected T getValue(ChangeNotifierJSpinner spinner){
		try{
			Model<T> model = (Model<T>) spinner.getModel();
			return model.getValue();
		} catch (Exception e){
			return null;
		}
	}
	
	@Override
	public boolean isValid(ChangeNotifierJSpinner spinner) {
		return this.isValid(this.getValue(spinner));
	}

	@Override
	public void process(P params, ChangeNotifierJSpinner spinner) {
		this.process(params, this.getValue(spinner));
	}

}
