package jprobe.framework.controller;

public interface ControllerObserver {
	
	public void workspaceAdded(WorkspaceController c);
	
	public void workspaceRemoved(WorkspaceController c);
	
}
