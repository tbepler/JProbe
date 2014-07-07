package jprobe.osgi.services;

import jprobe.framework.view.PersistentView;

public interface PersistentViewResource {
	
	public String uniqueId();
	public PersistentView newPersistentView();
	
}
