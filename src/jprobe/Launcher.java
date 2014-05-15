package jprobe;

import java.io.File;

public class Launcher {
	
	public static final String URL_PATH_SEPARATOR = "/";
	
	public static final String JAR_URL = Launcher.class.getProtectionDomain().getCodeSource().getLocation().getFile();
	public static final String JAR_DIR = JAR_URL.substring(0, JAR_URL.lastIndexOf(URL_PATH_SEPARATOR));
	public static final String CONFIG_FILE = JAR_DIR + File.separator + "config.txt";
	
	public static void main(String[] args){
		Configuration config = new Configuration(new File(CONFIG_FILE), args);
		//System.out.println(JAR_URL);
		//System.out.println(JAR_DIR);
		new JProbe(config);
	}
	
}


