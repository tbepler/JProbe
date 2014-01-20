package plugins.functions.gui;

import javax.swing.ImageIcon;

public class Constants {

	//public static final String RESOURCES_PATH = File.separator+"plugins"+File.separator+"functions"+File.separator+"gui"+File.separator+"resources";
	public static final String RESOURCES_PATH = "/plugins/functions/gui/resources";
	public static final String CHECK_ICON_PATH = RESOURCES_PATH +"/check.jpg";
	public static final String X_ICON_PATH = RESOURCES_PATH + "/x.jpg";
	
	public static final ImageIcon CHECK_ICON = new ImageIcon(Constants.class.getResource(CHECK_ICON_PATH));
	public static final ImageIcon X_ICON = new ImageIcon(Constants.class.getResource(X_ICON_PATH));
	
	
	
}
