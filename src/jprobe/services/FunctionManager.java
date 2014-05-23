package jprobe.services;

import jprobe.services.function.Function;

import org.osgi.framework.Bundle;

public interface FunctionManager {
	
	public void addListener(CoreListener listener);
	public void removeListener(CoreListener listener);
	
	public void addFunction(Function<?> f, Bundle responsible);
	public void removeFunction(Function<?> f, Bundle responsible);
	public Function<?>[] getAllFunctions();
	public Function<?>[] getFunctions(String name);
	public String[] getFunctionNames();
	public Bundle getProvider(Function<?> f);
	
}
