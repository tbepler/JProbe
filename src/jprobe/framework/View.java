package jprobe.framework;

import java.util.Properties;

public interface View {
	
	public void start(Controller controller, Properties props);
	
	public void stop(Controller controller, Properties props);
	
	public void waitForStop(long timeout) throws InterruptedException;
	
	//TODO
	
}
