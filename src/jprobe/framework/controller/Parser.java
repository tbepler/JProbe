package jprobe.framework.controller;

import util.progress.ProgressListener;

public interface Parser {
	
	public void execute(WorkspaceController workspace, String[] args, ProgressListener l);
	
}
