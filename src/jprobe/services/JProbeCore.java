package jprobe.services;

import java.io.File;


public interface JProbeCore {
	
	public void addCoreListener(CoreListener listener);
	public void removeCoreListener(CoreListener listener);
	
	public void shutdown();
	
	public Journal getLog();
	
	public Debug getDebugLevel();
	
	public ErrorHandler getErrorHandler();
	public void addErrorHandler(ErrorHandler handler);
	public void removeErrorHandler(ErrorHandler handler);
	
	public DataManager getDataManager();
	public FunctionManager getFunctionManager();
	
	public void save(File toFile);
	public void load(File fromFile);
	
	public void addSaveable(Saveable add);
	public void removeSaveable(Saveable remove);
	
}
