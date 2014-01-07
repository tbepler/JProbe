package jprobe.services;

import org.osgi.framework.Bundle;

public interface FunctionManager {
	
	public void addFunction(Function f, Bundle responsible);
	public void removeFunction(Function f, Bundle responsible);
	public Function[] getAllFunctions();
	public Function[] getFunctions(String name);
	public String[] getFunctionNames();
	
}
