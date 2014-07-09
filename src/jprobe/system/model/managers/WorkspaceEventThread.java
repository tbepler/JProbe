package jprobe.system.model.managers;

import util.WorkerThread;

public class WorkspaceEventThread extends WorkerThread{
	
	private static final String NAME = "WorkspaceEventThread-";
	private static int COUNT = 0;
	
	@Override
	protected String assignName(){
		synchronized(WorkspaceEventThread.class){
			return NAME + (COUNT++);
		}
	}
	
}
