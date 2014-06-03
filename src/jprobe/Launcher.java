package jprobe;

import java.io.File;
import java.io.IOException;

import jprobe.services.ErrorHandler;
import jprobe.services.Log;

public class Launcher {
	
	public static void main(String[] args){
		File log = initFile(Constants.JPROBE_LOG);
		Log.getInstance().init(new TimeStampJournal(log));
		File errorLog = initFile(Constants.JPROBE_ERROR_LOG);
		ErrorHandler.getInstance().init(new TimeStampJournal(errorLog));
		
		initDir(Constants.USER_PLUGINS_DIR);
		initDir(Constants.FELIX_CACHE_DIR);
		initDir(Constants.AUTOSAVE_DIR);
		initDir(Constants.PREFERENCES_DIR);
		
		Configuration config = new Configuration(new File(Constants.CONFIG_FILE), args);
		//System.out.println(JAR_URL);
		//System.out.println(JAR_DIR);
		new JProbe(config);
	}
	
	private static void initDir(String path){
		File f= new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
	}
	
	private static File initFile(String path){
		File f = new File(path);
		if(!f.exists()){
			f.getParentFile().mkdirs();
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.err.println("Error initializing file "+f + ": "+e.getMessage());
			}
		}
		return f;
	}
	
}


