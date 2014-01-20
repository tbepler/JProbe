package plugins.functions.gui.utils;

import javax.swing.JLabel;

public class ValidLabel extends JLabel implements StateListener{
	private static final long serialVersionUID = 1L;
	
	private ValidStateNotifier m_Observed;
	
	public ValidLabel(ValidStateNotifier observe){
		super();
		m_Observed = observe;
		m_Observed.addStateListener(this);
	}

	@Override
	public void update(StateNotifier source) {
		// TODO Auto-generated method stub
		
	}

}
