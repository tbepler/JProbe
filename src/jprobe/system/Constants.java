package jprobe.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.swing.ImageIcon;

import jprobe.system.launcher.Launcher;
import ch.qos.logback.classic.Level;

public class Constants {
	
	public static final String NAME = "JProbe";
	public static final String VERSION = "v0.5.1";
	public static final String AUTHOR = "Tristan Bepler";
	
	public static final String WORKSPACE_FILE_EXTENSION = "sav";
	public static final String WORKSPACE_DEFAULT_NAME = "Untitled Workspace";

	public static final String USER_HOME_DIR = System.getProperty("user.home");
	
	public static final String USER_DIR_NAME = "jprobe";
	public static final String PROPERTIES_DIR_NAME= "properties";
	public static final String LOG_DIR_NAME = "logs";
	
	public static final String LOG_NAME = "jprobe.log";
	public static final String LOG_NAME_PATTERN = "jprobe%i.log.zip";
	public static final int MAX_LOGS = 5;
	public static final String MAX_LOG_FILE_SIZE = "10MB";
	public static final String LOG_PATTERN = "%d %-5level [%thread] %logger: %msg%n";

	public static final String PROPERTIES_FILE_NAME = "jprobe.core.properties";
	public static final String PROPERTY_KEY_LOG_LEVEL = "rootLogLevel";
	public static final String DEFAULT_LOG_LEVEL = "INFO";
	public static final String PROPERTY_USER_DIR = Launcher.class + ".userDir";
	public static final String PROPERTY_SYSTEM_HELP = Controller.class + ".systemHelp";
	
	public static final Properties DEFAULT_PROPERTIES = createDefaultProperties();
			
	private static Properties createDefaultProperties(){
		Properties props = new Properties();
		props.setProperty(PROPERTY_KEY_LOG_LEVEL, DEFAULT_LOG_LEVEL);
		return props;
	}
	
	public static String getDefaultPropertiesComment(){
		StringBuilder builder = new StringBuilder();
		builder.append(NAME).append(" core properties.\n");
		builder.append("Valid log levels: ");
		builder.append(Level.OFF).append(", ").append(Level.ERROR).append(", ");
		builder.append(Level.WARN).append(", ").append(Level.INFO).append(", ");
		builder.append(Level.DEBUG).append(", ").append(Level.TRACE).append(", ");
		builder.append(Level.ALL);
		return builder.toString();
	}
	
	public static final String RESOURCES_PATH = "/jprobe/resources";
	
	public static final int DATA_TABLE_ROW_SAMPLE = 50;
	public static final int DATA_TABLE_MAX_COL_WIDTH = 400;
	
	public static final String INDEXED_FUNC_REGEX = "^.+\\[\\d+\\]$";
	public static final String SHORT_FLAG_PREFIX = "-";
	public static final String LONG_FLAG_PREFIX = "--";
	
	public static final String GUI_SFLAG = "-g";
	public static final String GUI_LFLAG = "--gui";
	public static final String GUI_REGEX = "("+GUI_SFLAG+")|("+GUI_LFLAG+")";
	
	public static final String HELP_LONG_FLAG = "help";
	public static final String HELP_SHORT_FLAG = "h";
	public static final String HELP_REGEX = "(" + SHORT_FLAG_PREFIX + HELP_SHORT_FLAG + ")|(" + LONG_FLAG_PREFIX + HELP_LONG_FLAG + ")";
	
	public static final String HELP_FILE_PATH = RESOURCES_PATH + "/help.txt";
	public static final String HELP_MESSAGE = readHelpMessage();
			
	private static String readHelpMessage(){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(Constants.class.getResourceAsStream(HELP_FILE_PATH)));
			String msg = reader.readLine();
			if(msg == null){
				reader.close();
				return "";
			}
			String line;
			while((line = reader.readLine()) != null){
				msg +=  "\n" + line;
			}
			reader.close();
			return msg;
		} catch (Exception e){
			System.out.println("Error on help file");
			e.printStackTrace();
			return "";
		}
	}
	
	public static final String X_PATH = RESOURCES_PATH+"/x.gif";
	public static final String X_HIGHLIGHTED_PATH = RESOURCES_PATH+"/xhighlight.gif";
	public static final String X_CLICKED_PATH = RESOURCES_PATH+"/xclicked.gif";
	
	public static final ImageIcon X_ICON = new ImageIcon(Constants.class.getResource(X_PATH));
	public static final ImageIcon X_HIGHLIGHTED_ICON = new ImageIcon(Constants.class.getResource(X_HIGHLIGHTED_PATH));
	public static final ImageIcon X_CLICKED_ICON = new ImageIcon(Constants.class.getResource(X_CLICKED_PATH));
	
}
