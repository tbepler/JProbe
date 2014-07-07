package jprobe.framework.controller;

import java.util.Properties;
import jprobe.Constants;
import jprobe.framework.model.Model;
import jprobe.services.data.Data;
import util.concurrent.Collector;
import util.concurrent.Result;
import util.progress.ProgressListener;

public interface Controller {
	
	public static final String PROPERTY_SYSTEM_HELP = Constants.PROPERTY_SYSTEM_HELP;
	
	public void start(Model model, Properties props);
	
	public void stop(Model model, Properties props);
	
	public void waitForStop(long timeout) throws InterruptedException;
	
	public void execute(String[] args, Collector<Result<Data>> collector,  ProgressListener l);
	
	//TODO
	
}
