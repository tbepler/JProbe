package jprobe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class Constants {
	
	public static final String NAME = "JProbe";
	public static final String VERSION = "v0.5.1";
	public static final String AUTHOR = "Tristan Bepler";
	
	public static final String HELP_FILE_PATH = "/jprobe/resources/help.txt";
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
	
	public static final String ARG_INTERACTIVE_MODE = "-interactive";
	public static final String ARG_COMMAND_MODE = "-command";
	public static final String ARG_HELP = "-help";
	
	public static final Collection<String> RESERVED_COMMANDS = generateReservedCommands();
	
	private static Collection<String> generateReservedCommands(){
		Collection<String> res = new HashSet<String>();
		res.add(ARG_INTERACTIVE_MODE);
		res.add(ARG_COMMAND_MODE);
		res.add(ARG_HELP);
		return Collections.unmodifiableCollection(res);
	}
	
}
