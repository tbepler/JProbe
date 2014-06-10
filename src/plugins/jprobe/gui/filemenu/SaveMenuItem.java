package plugins.jprobe.gui.filemenu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import plugins.jprobe.gui.GUIActivator;
import plugins.jprobe.gui.SaveLoadUtil;
import jprobe.services.JProbeCore;

public class SaveMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore m_Core;
	
	public SaveMenuItem(JProbeCore core){
		super("Save Workspace");
		m_Core = core;
		this.setMnemonic(KeyEvent.VK_S);
		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		SaveLoadUtil.save(m_Core, GUIActivator.getFrame());
	}

}
