package plugins.jprobe.gui.filemenu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import plugins.jprobe.gui.JProbeGUIFrame;

public class QuitMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	JProbeGUIFrame m_ParentFrame;
	
	public QuitMenuItem(JProbeGUIFrame parentFrame){
		super("Quit");
		m_ParentFrame = parentFrame;
		this.setMnemonic(KeyEvent.VK_Q);
		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		m_ParentFrame.quit();
	}
	
}
