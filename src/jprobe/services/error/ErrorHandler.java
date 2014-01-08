package jprobe.services.error;

import org.osgi.framework.Bundle;

public interface ErrorHandler {
	
	public void handleException(Bundle reporter, Exception e);
	public void handleException(Bundle reporter, String message);
	
}
