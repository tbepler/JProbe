package plugins.jprobe.gui;

import java.awt.Dimension;
import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

public class Constants {
	
	public static final String CONFIG_FILE_NAME = "jprobegui_config.txt";
	public static final File CONFIG_FILE = new File("plugins"+File.separator+CONFIG_FILE_NAME);
	
	public static final int DEFAULT_WIDTH = 1000;
	public static final int DEFAULT_HEIGHT = 800;
	
	public static final String[] SAVE_FILE_EXTENSIONS = new String[]{"sav"};
	public static final FileNameExtensionFilter SAVE_FILE_FILTER = new FileNameExtensionFilter("JProbe save file", SAVE_FILE_EXTENSIONS);
	
	public static final String PREF_APPLY_BUTTON_TEXT = "Apply";
	public static final String PREF_CANCEL_BUTTON_TEXT = "Cancel";
	
	public static final Dimension PREF_HELP_DEFAULT_DIM = new Dimension(600, 600);
	
}
