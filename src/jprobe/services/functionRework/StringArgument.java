package jprobe.services.functionRework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

public abstract class StringArgument<P> extends AbstractArgument<P> implements ActionListener{
	
	private final JTextField m_TextField;

	protected StringArgument(String name, String tooltip, String category, boolean optional, String startValue) {
		super(name, tooltip, category, optional);
		m_TextField = new JTextField(startValue);
		m_TextField.addActionListener(this);
	}
	
	protected abstract boolean isValid(String s);
	protected abstract P process(P params, String s);

	@Override
	public boolean isValid() {
		return this.isValid(m_TextField.getText());
	}

	@Override
	public JComponent getComponent() {
		return m_TextField;
	}

	@Override
	public P process(P params) {
		return process(params, m_TextField.getText());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_TextField){
			this.notifyListeners();
		}
	}

}
