package chiptools.jprobe.command;

import chiptools.Constants;
import jprobe.services.JProbeCore;
import jprobe.services.command.Command;

public class ProbeGenerator implements Command{
	
	private static final String USAGE = "";
	
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
