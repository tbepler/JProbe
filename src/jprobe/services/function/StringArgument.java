package jprobe.services.function;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import util.progress.ProgressListener;

public abstract class StringArgument<P> extends AbstractArgument<P> implements ActionListener{
	
	public static final String PROTOTYPE_TEXT = "some string here";
	
	private final String m_StartValue;
	//instantiate lazily
	private JTextField m_TextField = null;

	protected StringArgument(String name, String tooltip, String category, Character shortFlag, String prototypeVal, boolean optional, String startValue) {
		super(name, tooltip, category, shortFlag, prototypeVal, optional);
		m_StartValue = startValue;
	}
	
	private void initTextField(){
		m_TextField = new JTextField(PROTOTYPE_TEXT);
		Dimension size = m_TextField.getPreferredSize();
		m_TextField.setText(m_StartValue);
		m_TextField.setPreferredSize(size);
		m_TextField.setMinimumSize(size);
		m_TextField.addActionListener(this);
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
	public boolean isValid() {
		if(m_TextField == null) this.initTextField();
		return this.isValid(m_TextField.getText());
	}

	@Override
	public JComponent getComponent() {
		if(m_TextField == null) this.initTextField();
		return m_TextField;
	}

	@Override
	public void process(P params) {
		process(params, m_TextField.getText());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_TextField){
			this.notifyListeners();
		}
	}

}
