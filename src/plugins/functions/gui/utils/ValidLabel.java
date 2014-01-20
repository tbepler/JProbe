package plugins.functions.gui.utils;

import javax.swing.Icon;
import javax.swing.JLabel;

public class ValidLabel extends JLabel implements StateListener{
	private static final long serialVersionUID = 1L;
	
	private ValidStateNotifier m_Observed;
	private Icon m_ValidIcon;
	private Icon m_InvalidIcon;
	private boolean m_PrevState;
	
	public ValidLabel(ValidStateNotifier observe, Icon validIcon, Icon invalidIcon){
		super();
		m_Observed = observe;
		m_Observed.addStateListener(this);
		m_ValidIcon = validIcon;
		m_InvalidIcon = invalidIcon;
		if(m_Observed.isStateValid()){
			this.setIcon(m_ValidIcon);
		}else{
			this.setIcon(m_InvalidIcon);
		}
		m_PrevState = m_Observed.isStateValid();
		this.revalidate();
	}
	
	private void updateIcon(){
		if(m_PrevState == m_Observed.isStateValid()){
			return;
		}
		m_PrevState = m_Observed.isStateValid();
		if(m_Observed.isStateValid()){
			this.setIcon(m_ValidIcon);
		}else{
			this.setIcon(m_InvalidIcon);
		}
		this.revalidate();
	}

	@Override
	public void update(StateNotifier source) {
		this.updateIcon();
	}

}
