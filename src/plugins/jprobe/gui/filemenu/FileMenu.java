package plugins.jprobe.gui.filemenu;

import javax.swing.JFileChooser;
import javax.swing.JMenu;

import bepler.crossplatform.OS;
import bepler.crossplatform.Platform;
import plugins.jprobe.gui.JProbeGUIFrame;
import jprobe.services.JProbeCore;

public class FileMenu extends JMenu{
	private static final long serialVersionUID = 1L;
	
	public FileMenu(JProbeGUIFrame parentFrame, JProbeCore core, JFileChooser importChooser, JFileChooser exportChooser){
		super("File");
		this.add(new NewMenuItem(core));
		this.add(new OpenMenuItem(parentFrame, core));
		this.addSeparator();
		this.add(new SaveMenuItem(parentFrame, core));
		this.add(new SaveAsMenuItem(parentFrame, core));
		this.addSeparator();
		this.add(new ImportMenu(parentFrame, core, importChooser));
		this.add(new ExportMenu(parentFrame, core, exportChooser));
		//only add the quit menu if not on Mac
		if(Platform.getInstance().getOperatingSystem() != OS.MAC){
			this.addSeparator();
			this.add(new QuitMenuItem(parentFrame));
		}
	}
	
}
