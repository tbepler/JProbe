package plugins.functions.gui;

import javax.swing.ImageIcon;

public class Constants {

	//public static final String RESOURCES_PATH = File.separator+"plugins"+File.separator+"functions"+File.separator+"gui"+File.separator+"resources";
	public static final String RESOURCES_PATH = "/resources";
	public static final String CHECK_ICON_PATH = RESOURCES_PATH +"/check.jpg";
	public static final String X_ICON_PATH = RESOURCES_PATH + "/x.jpg";
	
	private static ImageIcon CHECK_ICON = null;
	private static ImageIcon X_ICON = null;
	
	public static ImageIcon getCheckIcon(){
		if(CHECK_ICON == null){
			CHECK_ICON = new ImageIcon(Constants.class.getResource(CHECK_ICON_PATH));
		}
		return CHECK_ICON;
	}
	
	public static ImageIcon getXIcon(){
		if(X_ICON == null){
			X_ICON = new ImageIcon(Constants.class.getResource(X_ICON_PATH));
		}
		return X_ICON;
	}
	
	public static final String ARGS_NAME_PROTOTYPE = "long name here";
	
	public static final double ARGS_PANEL_TARGET_ASPECT = 2.0;

	
}
