package plugins.jprobe.gui;

import java.awt.Dimension;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Constants {
	
	public static final String CONFIG_FILE_NAME = "jprobe-gui.pref";
	
	public static final int DEFAULT_WIDTH = 1000;
	public static final int DEFAULT_HEIGHT = 800;
	
	public static final String BACKGROUND_THREAD_NAME = "BackgroundThread";
	
	public static final int CENTER = -1;
	
	public static final String[] SAVE_FILE_EXTENSIONS = new String[]{"sav"};
	public static final FileNameExtensionFilter SAVE_FILE_FILTER = new FileNameExtensionFilter("JProbe save file (.sav)", SAVE_FILE_EXTENSIONS);
	
	public static final String AUTOSAVE_FILE_NAME = "autosave";
	public static final String AUTOSAVE_DIR_NAME = "autosaves";
	
	public static final String PREF_APPLY_BUTTON_TEXT = "Apply";
	public static final String PREF_CANCEL_BUTTON_TEXT = "Cancel";
	
	public static final Dimension PREF_HELP_DEFAULT_DIM = new Dimension(600, 600);
	
}
