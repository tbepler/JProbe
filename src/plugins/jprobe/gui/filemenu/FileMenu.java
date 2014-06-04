package plugins.jprobe.gui.filemenu;

import javax.swing.JMenu;

import plugins.jprobe.gui.JProbeGUIFrame;
import jprobe.services.JProbeCore;

public class FileMenu extends JMenu{
	private static final long serialVersionUID = 1L;
	
	public FileMenu(JProbeGUIFrame parentFrame, JProbeCore core){
		super("File");
		this.add(new NewMenuItem(core));
		this.add(new OpenMenuItem(core));
		this.addSeparator();
		this.add(new SaveMenuItem(core));
		this.add(new SaveAsMenuItem(core));
		this.addSeparator();
		this.add(new ImportMenu(core, parentFrame.getImportChooser()));
		this.add(new ExportMenu(core, parentFrame.getExportChooser()));
		this.addSeparator();
		this.add(new QuitMenuItem(parentFrame));
	}
	
}
