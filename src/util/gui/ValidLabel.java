package util.gui;

import javax.swing.Icon;
import javax.swing.JLabel;

import util.Observer;
import util.Subject;

public class ValidLabel extends JLabel implements Observer<Boolean>{
	private static final long serialVersionUID = 1L;
	
	private Subject<Boolean> m_Observed;
	private Icon m_ValidIcon;
	private Icon m_InvalidIcon;
	private boolean m_PrevState;
	
	public ValidLabel(Subject<Boolean> subject, boolean initialState, Icon validIcon, Icon invalidIcon){
		super();
		m_Observed = subject;
		m_Observed.register(this);
		m_ValidIcon = validIcon;
		m_InvalidIcon = invalidIcon;
		if(initialState){
			this.setIcon(m_ValidIcon);
		}else{
			this.setIcon(m_InvalidIcon);
		}
		m_PrevState = initialState;
	}
	
	private void updateIcon(boolean state){
		if(m_PrevState == state){
			return;
		}
		m_PrevState = state;
		if(state){
			this.setIcon(m_ValidIcon);
		}else{
			this.setIcon(m_InvalidIcon);
		}
		this.revalidate();
	}

	@Override
	public void update(Subject<Boolean> observed, Boolean notification) {
		if(observed == m_Observed){
			this.updateIcon(notification);
		}
	}

}
