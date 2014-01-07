package jprobe.services;

import jprobe.error.ErrorHandler;
import jprobe.log.Log;

public interface JProbeCore {
	
	public void addCoreListener(CoreListener listener);
	public void removeCoreListener(CoreListener listener);
	
	public void shutdown();
	
	public Log getLog();
	
	public ErrorHandler getErrorHandler();
	public void addErrorHandler(ErrorHandler handler);
	public void removeErrorHandler(ErrorHandler handler);
	
	public DataManager getDataManager();
	public FunctionManager getFunctionManager();
	
	public void addSaveable(Saveable add);
	public void removeSaveable(Saveable remove);
	
}
