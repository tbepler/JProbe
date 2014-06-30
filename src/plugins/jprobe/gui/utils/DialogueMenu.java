package plugins.jprobe.gui.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class DialogueMenu extends JMenu implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JDialog m_Dialog;
	private JMenuItem m_Button;
	
	public DialogueMenu(String name, JDialog dialog){
		super(name);
		this.m_Dialog = dialog;
		m_Button = new JMenuItem(name);
		m_Button.addActionListener(this);
		m_Button.setEnabled(true);
		m_Button.setVisible(true);
		this.add(m_Button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		m_Dialog.setVisible(true);
	}
	
	@Override
	public void setAccelerator(KeyStroke stroke){
		m_Button.setAccelerator(stroke);
	}
	
	@Override
	public void setMnemonic(int keycode){
		m_Button.setMnemonic(keycode);
		super.setMnemonic(keycode);
	}
	
}
