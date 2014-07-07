package jprobe.osgi.services;

import jprobe.framework.view.BatchView;

public interface BatchViewResource {
	
	public String uniqueId();
	public BatchView newBatchtView();
	
}
