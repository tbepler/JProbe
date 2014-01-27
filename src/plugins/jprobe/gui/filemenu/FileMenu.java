package plugins.jprobe.gui.filemenu;

import javax.swing.JMenu;

import jprobe.services.JProbeCore;

public class FileMenu extends JMenu{
	private static final long serialVersionUID = 1L;

	private JProbeCore m_Core;
	
	public FileMenu(JProbeCore core){
		super("File");
		m_Core = core;
		this.add(new ImportMenu(core));
	}
	
}
