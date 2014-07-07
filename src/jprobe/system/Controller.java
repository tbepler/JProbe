package jprobe.system;

import java.util.Properties;
import java.util.concurrent.Future;

import util.progress.ProgressListener;

public interface Controller {
	
	public static final String PROPERTY_SYSTEM_HELP = Constants.PROPERTY_SYSTEM_HELP;
	
	public void start(Model model, Properties props);
	
	public void stop(Model model, Properties props);
	
	public void waitForStop(long timeout) throws InterruptedException;
	
	public Future<?> parse(String[] args, ProgressListener l);
	
}
