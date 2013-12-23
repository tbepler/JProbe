package jprobe.log;

import org.osgi.framework.Bundle;

public interface Log {
	
	public void write(Bundle bundle, String output);
	
}
