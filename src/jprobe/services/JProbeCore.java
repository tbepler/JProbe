package jprobe.services;

import java.io.File;

import org.osgi.framework.Bundle;


public interface JProbeCore {
	
	public enum Mode{
		COMMAND,
		INTERACTIVE;
		
		@Override
		public String toString(){
			switch(this){
			case COMMAND:
				return "command";
			case INTERACTIVE:
				return "interactive";
			default:
				return null;
			}
		}
		
	}
	
	public Mode getMode();
	
	public void addCoreListener(CoreListener listener);
	public void removeCoreListener(CoreListener listener);
	
	public void shutdown();
	
	public DataManager getDataManager();
	public FunctionManager getFunctionManager();
	
	public void save(File toFile);
	public void load(File fromFile);
	
	public void addSaveable(Saveable add, Bundle bundle);
	public void removeSaveable(Saveable remove, Bundle bundle);
	
}
