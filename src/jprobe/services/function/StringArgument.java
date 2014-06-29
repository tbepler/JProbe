package jprobe.services.function;

import java.awt.Dimension;
import jprobe.services.Workspace;
import util.gui.ChangeNotifierTextField;
import util.progress.ProgressListener;

public abstract class StringArgument<P> extends AbstractArgument<P, ChangeNotifierTextField>{
	
	public static final String PROTOTYPE_TEXT = "some string here";
	
	private final String m_StartValue;

	protected StringArgument(String name, String tooltip, String category, Character shortFlag, String prototypeVal, boolean optional, String startValue) {
		super(name, tooltip, category, shortFlag, prototypeVal, optional);
		m_StartValue = startValue;
	}
	
	public ChangeNotifierTextField createComponent(Workspace w){
		ChangeNotifierTextField field = new ChangeNotifierTextField(PROTOTYPE_TEXT);
		Dimension size = field.getPreferredSize();
		field.setText(m_StartValue);
		field.setPreferredSize(size);
		field.setMinimumSize(size);
		return field;
	}
	
	protected abstract boolean isValid(String s);
	protected abstract void process(P params, String s);
	
	@Override
	public void parse(ProgressListener l, P params, String[] args){
		if(args.length < 1 || args.length > 1){
			throw new RuntimeException(this.getName() + " requires 1 argument. Received "+args.length);
		}
		if(!this.isValid(args[0])){
			throw new RuntimeException(this.getName() + " argument \""+args[0]+"\" is not valid");
		}
		this.process(params, args[0]);
	}

	@Override
	public boolean isValid(ChangeNotifierTextField field) {
		return this.isValid(field.getText());
	}

	@Override
	public void process(P params, ChangeNotifierTextField field) {
		process(params, field.getText());
	}

}
