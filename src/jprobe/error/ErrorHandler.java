package jprobe.error;

public interface ErrorHandler {
	
	public void handleException(Exception e);
	public void handleException(String message);
	
}
