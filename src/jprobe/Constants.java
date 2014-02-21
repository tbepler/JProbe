package jprobe;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class Constants {
	
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
