package jprobe.services;

import org.osgi.framework.Bundle;

public interface Journal {
	
	public void write(Bundle bundle, String output);
	
}
