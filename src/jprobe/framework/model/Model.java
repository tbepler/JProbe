package jprobe.framework.model;

import java.util.Properties;

public interface Model {
	
	public void start(Properties props);
	
	public void stop(Properties props);
	
	public void waitForStop(long timeout) throws InterruptedException;
	
	//TODO
	
}
