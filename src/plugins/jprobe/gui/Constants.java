package plugins.jprobe.gui;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.data.Data;

public class Constants {
	
	public static final String CONFIG_FILE_NAME = "jprobe-gui.pref";
	
	public static final int DEFAULT_WIDTH = 1000;
	public static final int DEFAULT_HEIGHT = 800;
	public static final Dimension DEFAULT_DIMENSION = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	
	public static final int DEFAULT_EXTENDED_STATE = Frame.NORMAL;
	
	public static final String BACKGROUND_THREAD_NAME = "BackgroundThread";
	
	public static final int CENTER = -1;
	
	public static final String[] SAVE_FILE_EXTENSIONS = new String[]{"sav"};
	public static final FileNameExtensionFilter SAVE_FILE_FILTER = new FileNameExtensionFilter("JProbe save file (.sav)", SAVE_FILE_EXTENSIONS);
	
	public static final String AUTOSAVE_FILE_NAME = "autosave";
	public static final String AUTOSAVE_DIR_NAME = "autosaves";
	
	public static final String PREF_APPLY_BUTTON_TEXT = "Apply";
	public static final String PREF_CANCEL_BUTTON_TEXT = "Cancel";
	
	public static final Dimension PREF_HELP_DEFAULT_DIM = new Dimension(600, 600);
	
	public static final String HELP_NAME = "Help";
	public static final String PREFERENCES_NAME = "Preferences";
	
	public static final String SAVE_APPROVE_TEXT = "Save";
	public static final String OPEN_APPROVE_TEXT = "Open";
	public static final String IMPORT_APPROVE_TEXT = "Import";
	public static final String EXPORT_APPROVE_TEXT = "Export";
	
	public static String getImportDataStart(Class<? extends Data> dataClass, String source){
		return "Importing "+dataClass.getSimpleName()+" from "+source;
	}
	public static String getImportDataError(Class<? extends Data> dataClass, String source){
		return "Error importing "+dataClass.getSimpleName()+" from "+source;
	}
	public static String getImportDataSuccess(Class<? extends Data> dataClass, String source){
		return "Imported "+dataClass.getSimpleName()+" from "+source;
	}
	
	public static String getExportDataStart(String dataName, String source){
		return "Exporting "+dataName+" to "+source;
	}
	public static String getExportDataError(String dataName, String source){
		return "Error exporting "+dataName+" to "+source;
	}
	public static String getExportDataSuccess(String dataName, String source){
		return "Exported "+dataName+" to "+source;
	}
	
	public static final String SAVE_WORKSPACE_START = "Saving workspace: ";
	public static final String SAVE_WORKSPACE_ERROR = "Error saving workspace: ";
	public static final String SAVE_WORKSPACE_SUCCESS = "Saved workspace: ";
	
	public static final String LOAD_WORKSPACE_START = "Loading workspace: ";
	public static final String LOAD_WORKSPACE_ERROR = "Error loading workspace: ";
	public static final String LOAD_WORKSPACE_SUCCESS = "Loaded workspace: ";

	public static final String OPEN_WORKSPACE_START = "Opening workspace: ";
	public static final String OPEN_WORKSPACE_ERROR = "Error opening workspace: ";
	public static final String OPEN_WORKSPACE_SUCCESS = "Opened workspace: ";
	
	public static final String IMPORT_WORKSPACE_START = "Importing workspace: ";
	public static final String IMPORT_WORKSPACE_ERROR = "Error importing workspace: ";
	public static final String IMPORT_WORKSPACE_SUCCESS = "Imported workspace: ";
	
}
