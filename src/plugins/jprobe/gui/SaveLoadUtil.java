package plugins.jprobe.gui;

import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import jprobe.services.JProbeCore;

public class SaveLoadUtil {
		
	public static final int PROCEED = 0;
	public static final int CANCEL = 1;
	
	private static final JFileChooser SAVE_LOAD_CHOOSER = new JFileChooser();
	
	private static File LAST_SAVE_FILE = null;
	
	public static void newWorkspace(JProbeCore core, Frame parent){
		if(unsavedWorkspaceCheck(core, parent) == PROCEED){
			core.newWorkspace();
			LAST_SAVE_FILE = null;
		}
	}
	
	public static int unsavedWorkspaceCheck(JProbeCore core, Frame parent){
		if(core.changedSinceLastSave()){
			int result = JOptionPane.showOptionDialog(
					parent,
					"You have unsaved changes to your current workspace.\n"+
							"Would you like to save before continuing?",
					"Unsaved changes",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,
					null,
					null
					);
			switch(result){
			case JOptionPane.CANCEL_OPTION:
				return CANCEL;
			case JOptionPane.YES_OPTION:
				return save(core, parent);
			default:
				break;
			}
		}
		return PROCEED;
	}
	
	public static int saveAs(JProbeCore core, Frame parent){
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
			return PROCEED;
		}
		return CANCEL;
	}
	
	public static int save(JProbeCore core, Frame parent){
		if(LAST_SAVE_FILE == null){
			return saveAs(core, parent);
		}else{
			if(core.changedSinceLastSave()){
				core.save(LAST_SAVE_FILE);
			}
			return PROCEED;
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
	
	public static void load(JProbeCore core, Frame parent){
		if(unsavedWorkspaceCheck(core, parent) == PROCEED){
			SAVE_LOAD_CHOOSER.resetChoosableFileFilters();
			SAVE_LOAD_CHOOSER.setFileFilter(Constants.SAVE_FILE_FILTER);
			int returnVal = SAVE_LOAD_CHOOSER.showDialog(parent, "Load");
			if(returnVal == JFileChooser.APPROVE_OPTION){
				core.load(SAVE_LOAD_CHOOSER.getSelectedFile());
			}
		}
	}
	
}
