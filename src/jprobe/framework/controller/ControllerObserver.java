package jprobe.framework.controller;

/**
 * This interface defines methods for receiving notifications from the controller
 * about new workspaces and closed workspaces.
 * 
 * @author Tristan Bepler
 *
 */
public interface ControllerObserver {
	
	/**
	 * Called when a new workspace is created.
	 * @param c - controller for that workspace
	 */
	public void workspaceAdded(WorkspaceController c);
	
	/**
	 * Called when a workspace has been stopped.
	 * @param c - controller that has  stopped
	 */
	public void workspaceRemoved(WorkspaceController c);
	
}
