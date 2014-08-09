package jprobe.framework.controller.parser;

import jprobe.framework.controller.WorkspaceController;
import util.progress.ProgressListener;

public class ParsingEngine implements Parser {

	@Override
	public void execute(WorkspaceController workspace, String args,
			ProgressListener l) {
		// TODO Auto-generated method stub
		
		String [] arguments = parseArgs(args);
		
	}
	
	private String [] parseArgs(String args){
		
		
		return args.split(" ");
	}

}
