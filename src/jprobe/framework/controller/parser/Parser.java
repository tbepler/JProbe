package jprobe.framework.controller.parser;

import jprobe.framework.controller.WorkspaceController;
import util.progress.ProgressListener;

public interface Parser {
	public static final String SEP = "\\s";
	public static final char LITERAL_OPEN = '"';
	public static final char LITERAL_CLOSE = '"';
	public static final char TUPLE_OPEN = '(';
	public static final char TUPLE_CLOSE = ')';
	public static final char ESCAPE_STR = '\\';
	
	public static final int LITERAL = 0;
	public static final int TUPLE = 1;
	public static final int VARIABLE = 2;
	
	public void execute(WorkspaceController workspace, String args, ProgressListener l);
	
}
