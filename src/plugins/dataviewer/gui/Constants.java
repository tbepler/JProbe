package plugins.dataviewer.gui;

import javax.swing.ImageIcon;

public class Constants {
	
	public static final String RESOURCES_PATH = "/plugins/dataviewer/gui/resources";
	public static final String X_PATH = RESOURCES_PATH+"/x.gif";
	public static final String X_HIGHLIGHTED_PATH = RESOURCES_PATH+"/xhighlight.gif";
	public static final String X_CLICKED_PATH = RESOURCES_PATH+"/xclicked.gif";
	
	public static final ImageIcon X_ICON = new ImageIcon(Constants.class.getResource(X_PATH));
	public static final ImageIcon X_HIGHLIGHTED_ICON = new ImageIcon(Constants.class.getResource(X_HIGHLIGHTED_PATH));
	public static final ImageIcon X_CLICKED_ICON = new ImageIcon(Constants.class.getResource(X_CLICKED_PATH));
	
	public static final String[] DATALIST_COL_HEADERS = new String[]{"Name", "Type"};
	
}
