package chiptools.jprobe.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import chiptools.Constants;
import jprobe.services.JProbeCore;
import jprobe.services.command.Command;

public class ProbeGenerator implements Command{
	
	public static final String USAGE_FILE = "probegen_usage.txt";
	
	private static final String USAGE = readUsage();
	
	private static String readUsage(){
		String usage = "Author: "+Constants.AUTHOR+"\n";
		BufferedReader reader = new BufferedReader(new InputStreamReader(PeakFinder.class.getResourceAsStream(Constants.RESOURCES_PATH+"/"+USAGE_FILE)));
		String line;
		try {
			while((line = reader.readLine()) != null){
				usage += "\n"+line;
			}
			usage += "\n";
		} catch (IOException e) {
			//stuff
		}
		return usage;
	}
	
	private final String NAME = Constants.CMD_NAMES.containsKey(ProbeGenerator.class) ? Constants.CMD_NAMES.get(ProbeGenerator.class) : "probegen";
	private final String DESCRIPTION = Constants.CMD_DESCRIPTIONS.containsKey(ProbeGenerator.class) ? 
			Constants.CMD_DESCRIPTIONS.get(ProbeGenerator.class) : 
			"Error finding description";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
	
	protected void printUsage(){
		System.out.println(NAME+" - "+DESCRIPTION);
		System.out.println(USAGE);
	}

	@Override
	public void execute(JProbeCore core, String[] args) {
		// TODO Auto-generated method stub
		
	}

}
