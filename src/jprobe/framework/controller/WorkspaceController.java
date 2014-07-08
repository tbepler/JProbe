package jprobe.framework.controller;

import java.util.Properties;

import util.concurrent.Collector;
import util.concurrent.Result;
import util.progress.ProgressListener;
import jprobe.framework.Stoppable;
import jprobe.framework.model.WorkspaceModel;
import jprobe.services.data.Data;

public interface WorkspaceController extends Stoppable{
	
	public void start(WorkspaceModel model, Properties props);
	
	public void execute(String[] args, Collector<Result<Data>> collector,  ProgressListener l);
	
}
