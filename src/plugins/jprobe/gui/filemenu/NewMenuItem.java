package plugins.jprobe.gui.filemenu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jprobe.services.JProbeCore;

public class NewMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;

	private final JProbeCore m_Core;
	
	public NewMenuItem(JProbeCore core){
		super("New Workspace");
		m_Core = core;
		this.addActionListener(this);
		this.setMnemonic(KeyEvent.VK_N);
		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		m_Core.newWorkspace();
	}

}
