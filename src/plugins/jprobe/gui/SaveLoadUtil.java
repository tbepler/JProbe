package plugins.jprobe.gui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import jprobe.services.JProbeCore;

public class SaveLoadUtil {
		
	private static final JFileChooser SAVE_LOAD_CHOOSER = new JFileChooser();
	
	private static File LAST_SAVE_FILE = null;
	
	public static void saveAs(JProbeCore core, Component parent){
		SAVE_LOAD_CHOOSER.resetChoosableFileFilters();
		SAVE_LOAD_CHOOSER.setFileFilter(Constants.SAVE_FILE_FILTER);
		int returnVal = SAVE_LOAD_CHOOSER.showDialog(parent, "Save");
		if(returnVal == JFileChooser.APPROVE_OPTION){
			String fileName = SAVE_LOAD_CHOOSER.getSelectedFile().toString();
			if(!stringEndsWithValidExtension(fileName)){
				fileName += "." + Constants.SAVE_FILE_EXTENSIONS[0];
			}
			LAST_SAVE_FILE = new File(fileName);
			core.save(LAST_SAVE_FILE);
		}
	}
	
	public static void save(JProbeCore core, Component parent){
		if(LAST_SAVE_FILE == null){
			saveAs(core, parent);
		}else{
			core.save(LAST_SAVE_FILE);
		}
	}
	
	public static boolean stringEndsWithValidExtension(String fileName){
		for(String ext : Constants.SAVE_FILE_EXTENSIONS){
			if(fileName.endsWith("."+ext)){
				return true;
			}
		}
		return false;
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
