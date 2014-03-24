package chiptools.jprobe.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import chiptools.Constants;
import jprobe.services.command.Command;

public abstract class AbstractCommand implements Command{
	
	private final String m_Name;
	private final String m_Description;
	private final String m_Usage;
	
	protected AbstractCommand(String defaultName, String defaultDescription, String usageFile){
		m_Name = Constants.CMD_NAMES.containsKey(this.getClass()) ? Constants.CMD_NAMES.get(this.getClass()) : defaultName;
		m_Description = Constants.CMD_DESCRIPTIONS.containsKey(this.getClass())
				? Constants.CMD_DESCRIPTIONS.get(this.getClass())
				: defaultDescription;
		m_Usage = this.readUsage(usageFile);
	}
	
	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public String getDescription() {
		return m_Description;
	}
	
	protected void printUsage(){
		System.out.println(m_Name+" - "+m_Description);
		System.out.println(m_Usage);
	}
	
	protected String readUsage(String usageFile){
		String usage = "Author: "+Constants.AUTHOR+"\n";
		BufferedReader reader = new BufferedReader(new InputStreamReader(PeakFinder.class.getResourceAsStream(Constants.RESOURCES_PATH+"/"+usageFile)));
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

}
