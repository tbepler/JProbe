package jprobe.services;

import org.osgi.framework.Bundle;

public interface ErrorManager {
	
	public void handleException(Exception e, Bundle thrower);
	public void handleWarning(String warning, Bundle thrower);
	
}
