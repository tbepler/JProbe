package jprobe.framework.controller;

import java.util.Collection;
import java.util.Properties;

import jprobe.Constants;
import jprobe.framework.Stoppable;
import jprobe.framework.model.Model;
import util.progress.ProgressListener;

public interface Controller extends Stoppable{
	
	public static final String PROPERTY_SYSTEM_HELP = Constants.PROPERTY_SYSTEM_HELP;
	
	public void start(Model model, Properties props);
	
	public void newWorkspace();
	
	public void openWorkspace(Source s, ProgressListener l);
	
	public Collection<WorkspaceController> getWorkspaces();
	
	
	//TODO
	
}
