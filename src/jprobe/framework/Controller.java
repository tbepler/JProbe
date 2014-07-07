package jprobe.framework;

import java.util.Properties;
import java.util.concurrent.Future;

import jprobe.Constants;
import util.progress.ProgressListener;

public interface Controller {
	
	public static final String PROPERTY_SYSTEM_HELP = Constants.PROPERTY_SYSTEM_HELP;
	
	public void start(Model model, Properties props);
	
	public void stop(Model model, Properties props);
	
	public void waitForStop(long timeout) throws InterruptedException;
	
	public Future<?> execute(String[] args, ProgressListener l);
	
	//TODO
	
}
