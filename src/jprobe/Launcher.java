package jprobe;

import java.io.File;

public class Launcher {
	
	public static final String CONFIG_FILE = "config.txt";
	
	public static void main(String[] args){
		Configuration config = new Configuration(new File(CONFIG_FILE), args);
		new JProbe(config);
	}
	
}


