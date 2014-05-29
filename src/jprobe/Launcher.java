package jprobe;

import java.io.File;

public class Launcher {
	
	public static void main(String[] args){
		Configuration config = new Configuration(new File(Constants.CONFIG_FILE), args);
		//System.out.println(JAR_URL);
		//System.out.println(JAR_DIR);
		new JProbe(config);
	}
	
}


