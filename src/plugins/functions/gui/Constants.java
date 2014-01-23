package plugins.functions.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

public class Constants {

	//public static final String RESOURCES_PATH = File.separator+"plugins"+File.separator+"functions"+File.separator+"gui"+File.separator+"resources";
	public static final String RESOURCES_PATH = "/plugins/functions/gui/resources";
	public static final String CHECK_ICON_PATH = RESOURCES_PATH +"/check.jpg";
	public static final String X_ICON_PATH = RESOURCES_PATH + "/x.jpg";
	
	public static final ImageIcon CHECK_ICON = new ImageIcon(Constants.class.getResource(CHECK_ICON_PATH));
	public static final ImageIcon X_ICON = new ImageIcon(Constants.class.getResource(X_ICON_PATH));
	
	public static final int SPINNER_MIN_COLS = 5;
	public static final int SPINNER_MAX_COLS = 10;
	public static final int TEXTFIELD_NUM_COLS = 10;
	
	public static final String DATAPANEL_TITLE = "Data";
	public static final List<String> DATAPANEL_HEADER = generateDataPanelHeader();
	
	private static List<String> generateDataPanelHeader(){
		List<String> header = new ArrayList<String>();
		header.add("Name");
		header.add("Optional?");
		header.add("Valid");
		header.add("Data");
		return Collections.unmodifiableList(header);
	}
	
	public static final String FIELDPANEL_TITLE = "Fields";
	public static final List<String> FIELDPANEL_HEADER = generateFieldPanelHeader();
			
	private static List<String> generateFieldPanelHeader(){
		List<String> header = new ArrayList<String>();
		header.add("Name");
		header.add("Optional?");
		header.add("Valid");
		header.add("Field");
		return Collections.unmodifiableList(header);
	}
	
}
