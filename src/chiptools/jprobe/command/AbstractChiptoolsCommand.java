package chiptools.jprobe.command;

import java.io.PrintStream;

import chiptools.Constants;

public abstract class AbstractChiptoolsCommand extends jprobe.services.command.AbstractCommand{
	
	private static String lookupName(Class<? extends AbstractChiptoolsCommand> clazz, String defaultName){
		return Constants.CMD_NAMES.containsKey(clazz) ? Constants.CMD_NAMES.get(clazz) : defaultName;
	}
	
	private static String lookupDescription(Class<? extends AbstractChiptoolsCommand> clazz, String defaultDescription){
		return Constants.CMD_DESCRIPTIONS.containsKey(clazz)
				? Constants.CMD_DESCRIPTIONS.get(clazz)
				: defaultDescription;
	}
	
	protected AbstractChiptoolsCommand(Class<? extends AbstractChiptoolsCommand> clazz, String defaultName, String defaultDescription) {
		super(lookupName(clazz, defaultName), lookupDescription(clazz, defaultDescription));
	}
	
	protected void printUsage(PrintStream ps){
		ps.println(this.getName() + " - " + this.getDescription());
		ps.println("Author: "+Constants.AUTHOR);
		ps.println("\n"+this.getUsage());
	}

}
