package jprobe.services;

import java.util.Properties;

public interface CorePluginService {
	
	public void start(JProbeCore core, Properties properties);
	
	public void stop(JProbeCore core, Properties properties);
	
}
