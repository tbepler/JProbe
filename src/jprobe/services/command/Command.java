package jprobe.services.command;

import jprobe.services.JProbeCore;

public interface Command {

	public String getName();
	public String getDescription();
	public void execute(JProbeCore core, String[] args);
	
}
