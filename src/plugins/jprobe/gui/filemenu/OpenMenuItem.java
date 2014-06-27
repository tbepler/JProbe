package plugins.jprobe.gui.filemenu;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import plugins.jprobe.gui.SaveLoadUtil;
import jprobe.services.JProbeCore;

public class OpenMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private final Frame m_Parent;
	private final JProbeCore m_Core;
	
	public OpenMenuItem(Frame parent, JProbeCore core){
		super("Open Workspace");
		m_Parent = parent;
		m_Core = core;
		this.addActionListener(this);
		this.setMnemonic(KeyEvent.VK_O);
		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		SaveLoadUtil.load(m_Core, m_Parent);
	}

}
