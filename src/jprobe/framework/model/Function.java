package jprobe.framework.model;

import java.util.List;
import java.util.Map;

public interface Function<R> {
	
	public List<Parameter<?>> getParameters();
	
	public <T> Function<R> putArgument(Parameter<T> param, Function<T> arg);
	
	public R call();
	
	public R call(Function<?> ... args);
	
	public R call(List<Function<?>> args);
	
	public R call(Map<Parameter<?>, Function<?>> args);

}
