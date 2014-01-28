package plugins.jprobe.gui;

import java.awt.Component;

import javax.swing.JFileChooser;

import jprobe.services.JProbeCore;

public class SaveLoadUtil {
		
	private static final JFileChooser SAVE_LOAD_CHOOSER = new JFileChooser();
	
	public static void save(JProbeCore core, Component parent){
		SAVE_LOAD_CHOOSER.resetChoosableFileFilters();
		SAVE_LOAD_CHOOSER.setFileFilter(Constants.SAVE_FILE_FILTER);
		int returnVal = SAVE_LOAD_CHOOSER.showDialog(parent, "Save");
		if(returnVal == JFileChooser.APPROVE_OPTION){
			core.save(SAVE_LOAD_CHOOSER.getSelectedFile());
		}
	}
	
	public static void load(JProbeCore core, Component parent){
		SAVE_LOAD_CHOOSER.resetChoosableFileFilters();
		SAVE_LOAD_CHOOSER.setFileFilter(Constants.SAVE_FILE_FILTER);
		int returnVal = SAVE_LOAD_CHOOSER.showDialog(parent, "Load");
		if(returnVal == JFileChooser.APPROVE_OPTION){
			core.load(SAVE_LOAD_CHOOSER.getSelectedFile());
		}
	}
	
}
