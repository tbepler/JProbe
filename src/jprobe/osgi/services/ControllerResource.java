package jprobe.osgi.services;

import jprobe.framework.controller.Controller;

public interface ControllerResource {
	
	public String uniqueId();
	public Controller newController();
	
}
