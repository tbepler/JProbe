package jprobe.services.function.components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

public class IconButton extends JButton implements MouseListener{
	private static final long serialVersionUID = 1L;
	
	private Icon m_Default;
	private Icon m_Hover;
	private Icon m_Pressed;
	
	public IconButton(Icon def, Icon hover, Icon pressed){
		super();
		m_Default = def;
		m_Hover = hover;
		m_Pressed = pressed;
		this.setIcon(m_Default);
		this.addMouseListener(this);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setContentAreaFilled(false);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		//do nothing
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		this.setIcon(m_Hover);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		this.setIcon(m_Default);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		this.setIcon(m_Pressed);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(this.getIcon() == m_Pressed){
			this.setIcon(m_Hover);
		}
	}
	
}
