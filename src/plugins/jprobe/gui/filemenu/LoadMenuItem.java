package plugins.jprobe.gui.filemenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import plugins.jprobe.gui.GUIActivator;
import plugins.jprobe.gui.SaveLoadUtil;
import jprobe.services.JProbeCore;

public class LoadMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore m_Core;
	
	public LoadMenuItem(JProbeCore core){
		super("Load");
		m_Core = core;
		this.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		SaveLoadUtil.load(m_Core, GUIActivator.getFrame());
	}

}
