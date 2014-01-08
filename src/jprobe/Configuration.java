package jprobe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class is responsible for parsing the configuration file. The file should be formatted as:
 * <p>
 * Tag: Value
 * <p>
 * The name of the tag is not case sensitive. Valid tags are:
 * <p>
 * 
 * @author Tristan Bepler
 *
 */
public class Configuration {
	
	private static final Debug DEFAULT_DEBUG_LEVEL = Debug.LOG;
	private static final String DEFAULT_STORAGE_CLEAN = "onFirstInit";
	private static final String DEFAULT_AUTODEPLOY_DIRECTORY = "plugins";
	private static final String DEFAULT_LOG_FILE = "jprobe.log";
	private static final String DEFAULT_ERROR_LOG_FILE = "jprobe_error.log";
		
	public static final String TAG_DEBUG_LEVEL = "debug";
	public static final String TAG_STORAGE_CLEAN = "felix_storage_clean";
	public static final String TAG_AUTODEPLOY_DIRECTORY = "autodeploy_plugin_directory";
	public static final String TAG_LOG_FILE = "log_file";
	public static final String TAG_ERROR_LOG_FILE = "error_log_file";
	
	private static final String DEFAULT_FILE = "//debug values: 0=off, 1=log, 2=full\n"+TAG_DEBUG_LEVEL+": "+
	DEFAULT_DEBUG_LEVEL+"\n"+TAG_AUTODEPLOY_DIRECTORY+": "+DEFAULT_AUTODEPLOY_DIRECTORY+"\n"+TAG_STORAGE_CLEAN+
	": "+DEFAULT_STORAGE_CLEAN+"\n"+TAG_LOG_FILE+": "+DEFAULT_LOG_FILE+"\n"+TAG_ERROR_LOG_FILE+": "+DEFAULT_ERROR_LOG_FILE;
	
	private enum Tag{
		DEBUG,
		STORAGE_CLEAN,
		AUTODEPLOY,
		LOG_FILE,
		ERROR_FILE,
		OTHER;
		
		public static Tag fromString(String s){
			if(s.equalsIgnoreCase(TAG_AUTODEPLOY_DIRECTORY)){
				return AUTODEPLOY;
			}
			if(s.equalsIgnoreCase(TAG_DEBUG_LEVEL)){
				return DEBUG;
			}
			if(s.equalsIgnoreCase(TAG_ERROR_LOG_FILE)){
				return ERROR_FILE;
			}
			if(s.equalsIgnoreCase(TAG_LOG_FILE)){
				return LOG_FILE;
			}
			if(s.equalsIgnoreCase(TAG_STORAGE_CLEAN)){
				return STORAGE_CLEAN;
			}
			return OTHER;
		}
	}
	
	private Debug debugLevel = DEFAULT_DEBUG_LEVEL;
	private String felixStorageClean = DEFAULT_STORAGE_CLEAN;
	private String autoDeployPluginDirectory = DEFAULT_AUTODEPLOY_DIRECTORY;
	private String logFile = DEFAULT_LOG_FILE;
	private String errorLogFile = DEFAULT_ERROR_LOG_FILE;
	
	public Configuration(File configFile){
		try {
			Scanner s = new Scanner(configFile);
			while(s.hasNextLine()){
				readLine(s.nextLine());
			}
		} catch (FileNotFoundException e) {
			System.err.println("Error: unable to find configuration file "+configFile.getAbsolutePath());
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
				writer.write(DEFAULT_FILE);
				writer.close();
			} catch (IOException e1) {
				//do nothing
			}
		}
		
	}

	private void readLine(String line){
		if(line.startsWith("//")){
			return;
		}
		try{
			String tag = line.substring(0, line.indexOf(':')).trim();
			String value = line.substring(line.indexOf(':')+1).trim();
			switch(Tag.fromString(tag)){
			case DEBUG:
				debugLevel = Debug.fromString(value);
				break;
			case STORAGE_CLEAN:
				felixStorageClean = value;
				break;
			case AUTODEPLOY:
				autoDeployPluginDirectory = value;
				break;
			case LOG_FILE:
				logFile = value;
				break;
			case ERROR_FILE:
				errorLogFile = value;
				break;
			default:
				break;
			}
		} catch (Exception e){
			System.err.println("Error: unable to read line \""+line+"\" in configuration file");
			e.printStackTrace();
			//do nothing, line was unreadable
		}
	}
	
	public String getErrorLogFile(){
		return errorLogFile;
	}
	
	public String getLogFile(){
		return logFile;
	}
	
	public String getAutoDeployPluginDirectory(){
		return autoDeployPluginDirectory;
	}
	
	public String getFelixStorageClean(){
		return felixStorageClean;
	}
	
	public Debug getDebugLevel(){
		return debugLevel;
	}
	
}
