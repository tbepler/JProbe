package jprobe.services;

import jprobe.services.function.FunctionPrototype;

import org.osgi.framework.Bundle;

public interface FunctionManager {
	
	public void addListener(CoreListener listener);
	public void removeListener(CoreListener listener);
	
	public void addFunctionPrototype(FunctionPrototype f, Bundle responsible);
	public void removeFunctionPrototype(FunctionPrototype f, Bundle responsible);
	public FunctionPrototype[] getAllFunctionPrototypes();
	public FunctionPrototype[] getFunctionPrototypes(String name);
	public String[] getFunctionNames();
	
}
