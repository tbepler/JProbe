package plugins.dataviewer.gui;

import javax.swing.ImageIcon;

public class Constants {
	
	public static final String RESOURCES_PATH = "/plugins/dataviewer/gui/resources";
	public static final String X_PATH = RESOURCES_PATH+"/x.gif";
	public static final String X_HIGHLIGHTED_PATH = RESOURCES_PATH+"/xhighlight.gif";
	public static final String X_CLICKED_PATH = RESOURCES_PATH+"/xclicked.gif";
	
	private static ImageIcon X_ICON = null;
	private static ImageIcon X_HIGHLIGHTED_ICON = null;
	private static ImageIcon X_CLICKED_ICON = null;
	
	public static ImageIcon getXIcon(){
		if(X_ICON == null){
			X_ICON = new ImageIcon(Constants.class.getResource(X_PATH));
		}
		return X_ICON;
	}
	
	public static ImageIcon getXHighlightedIcon(){
		if(X_HIGHLIGHTED_ICON == null){
			X_HIGHLIGHTED_ICON = new ImageIcon(Constants.class.getResource(X_HIGHLIGHTED_PATH));
		}
		return X_HIGHLIGHTED_ICON;
	}
	
	public static ImageIcon getXClickedIcon(){
		if(X_CLICKED_ICON == null){
			X_CLICKED_ICON = new ImageIcon(Constants.class.getResource(X_CLICKED_PATH));
		}
		return X_CLICKED_ICON;
	}
	
	public static final String[] DATALIST_COL_HEADERS = new String[]{"Name", "Type"};
	public static final int DATALIST_MIN_COL_WIDTH = 100;
	
}
