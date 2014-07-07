package jprobe.osgi.services;

import jprobe.framework.model.Model;

public interface ModelResource {
	
	public String uniqueId();
	public Model newModel();
	
}
