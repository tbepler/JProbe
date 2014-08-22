package jprobe;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import javax.swing.ImageIcon;

public class Constants {
	
	public static final String NAME = "JProbe";
	public static final String VERSION = "v0.5.1";
	public static final String AUTHOR = "Tristan Bepler";
	
	public static final String WORKSPACE_FILE_EXTENSION = "sav";

	public static final String URL_PATH_SEPARATOR = "/";
	
	public static final String JAR_URL = Launcher.class.getProtectionDomain().getCodeSource().getLocation().getFile();
	public static final String JAR_DIR = JAR_URL.substring(0, JAR_URL.lastIndexOf(URL_PATH_SEPARATOR));
	
	public static final String PLUGIN_AUTODEPLOY = JAR_DIR + File.separator + "plugins";
	
	public static final String FELIX_EXPORT_PACKAGES = "jprobe.services;version=1.0.0," +
			"jprobe.services.data;version=1.0.0," +
			"jprobe.services.function;version=1.0.0,"
			+ "jprobe.services.function.components;version=1.0.0,"
			+ "bepler.crossplatform;version=1.0.0,"
			+ "util.progress;version=1.0.0,"
			+ "util.gui;version=1.0.0,"
			+ "util;version=1.0.0,"
			+ "util.genome;version=1.0.0,"
			+ "util.genome.reader;version=1.0.0,"
			+ "util.genome.reader.query;version=1.0.0,"
			+ "util.genome.reader.threaded;version=1.0.0,"
			+ "util.genome.peak;version=1.0.0,"
			+ "util.genome.kmer;version=1.0.0,"
			+ "util.genome.probe;version=1.0.0,"
			+ "util.genome.pwm;version=1.0.0,"
			+ "util.xmlserializer;version=1.0.0";
	
	public static final String USER_HOME_DIR = System.getProperty("user.home");
	public static String USER_JPROBE_DIR = USER_HOME_DIR + File.separator + "jprobe";
	
	public static String USER_PLUGINS_DIR = USER_JPROBE_DIR + File.separator + "plugins";
	
	public static String FELIX_CACHE_DIR = USER_JPROBE_DIR + File.separator + "cache";
	
	public static final String FELIX_FILE_INSTALL_DIR_PROP = "felix.fileinstall.dir";
	public static final String FELIX_WATCH_DIRS = USER_PLUGINS_DIR;
	public static final String FELIX_FILE_INSTALL_INITIALDELAY_PROP = "felix.fileinstall.noInitialDelay";
	public static final String FELIX_INITIALDELAY = "true";
	
	public static String PREFERENCES_DIR = USER_JPROBE_DIR + File.separator + "preferences";
	public static String CONFIG_FILE = PREFERENCES_DIR + File.separator + "jprobe.pref";
	
	public static String LOG_DIR = USER_JPROBE_DIR + File.separator + "logs";
	public static String JPROBE_LOG = LOG_DIR + File.separator + "jprobe.log";
	public static String JPROBE_ERROR_LOG = LOG_DIR + File.separator + "jprobe-error.log";
	
	public static final String RESOURCES_PATH = "/jprobe/resources";
	
	public static final int DATA_TABLE_ROW_SAMPLE = 200;
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
