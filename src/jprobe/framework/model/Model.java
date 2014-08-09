package jprobe.framework.model;

import java.util.Properties;

import jprobe.framework.Stoppable;

public interface Model extends Stoppable{
	
	public void start(Properties props);
	
	public void register(ModelListener l);
	
	public void unregister(ModelListener l);
	
	public Workspace newWorkspace();
	
	
	
	//TODO
	
}
