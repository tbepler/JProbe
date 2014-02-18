package plugins.genome;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

public class Constants {
	
	public static final String RUN_BUTTON_TEXT = "Run";
	public static final String CANCEL_BUTTON_TEXT = "Cancel";
	
	public static final Dimension GENOME_BUTTON_SPACING = new Dimension(5,0);
	public static final Insets GENOME_BUTTON_INSETS = new Insets(2,2,2,2);
	
	public static final String GENOME_FILE_DIR_DEFAULT = "";
	public static final String GENOME_FILE_BUTTON_TEXT = "Browse";
	public static final String GENOME_FILE_PANEL_TITLE = "Genome File";
	public static final Border GENOME_FILE_PANEL_BORDER = BorderFactory.createTitledBorder(GENOME_FILE_PANEL_TITLE);
	
	public static final String RESOURCES_PATH = "resources";
	public static final String CHECK_PATH = RESOURCES_PATH + "/check.jpg";
	public static final String X_PATH = RESOURCES_PATH + "/x.jpg";
	
	public static final ImageIcon CHECK_ICON = new ImageIcon(Constants.class.getResource(CHECK_PATH));
	public static final ImageIcon X_ICON = new ImageIcon(Constants.class.getResource(X_PATH));
	
}
