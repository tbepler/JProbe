package jprobe.framework.view;

import java.util.Properties;

import jprobe.framework.controller.Controller;

public interface PersistentView {
	
	public void start(Controller controller, Properties props);
	
	public void stop(Controller controller, Properties props);
	
	public void waitForStop(long timeout) throws InterruptedException;
	
	//TODO
	
}
