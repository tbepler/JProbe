package jprobe.framework.controller;

import java.util.Collection;
import java.util.Properties;

import util.concurrent.Collector;
import util.concurrent.Result;
import util.progress.ProgressListener;
import jprobe.framework.Stoppable;
import jprobe.framework.model.FunctionDefinition;
import jprobe.framework.model.Workspace;
import jprobe.services.data.Data;

public interface WorkspaceController extends Stoppable{
	
	public void start(Workspace model, Properties props);
	
	public Collection<FunctionDefinition<?,?>> getFunctions();
	public Collection<Readable> getReadables();
	
	public void execute(String[] args, Collector<Result<Data>> collector,  ProgressListener l);
	public <P,D> void execute(FunctionDefinition<P,D> f, P params, Collector<Result<D>> collector, ProgressListener l);
	public void execute(Readable read, FormattedSource source, Collector<Result<Data>> collector, ProgressListener l);
	
	
	
}
