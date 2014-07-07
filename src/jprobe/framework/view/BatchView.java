package jprobe.framework.view;

import java.util.Properties;

import jprobe.framework.controller.Controller;

public interface BatchView {
	
	public void start(Controller controller, Properties props, String[] args);
	
	public void stop(Controller controller, Properties props);
	
	public void waitForStop(long timeout) throws InterruptedException;
	
	//TODO
	
}
