package jprobe.framework.model;

public interface ModelListener {
	
	/**
	 * Called by the {@link Model} when a {@link Workspace}
	 * is opened.
	 * @param w
	 */
	public void workspaceOpened(Workspace w);
	
	/**
	 * Called by the {@link Model} when a {@link Workspace}
	 * is closed.
	 * @param w
	 */
	public void workspaceClosed(Workspace w);
	
}
