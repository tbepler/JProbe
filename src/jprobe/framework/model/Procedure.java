package jprobe.framework.model;

import java.util.List;
import java.util.Map;

public interface Procedure<R> {
	
	public List<Parameter<?>> getParameters();
	
	public Class<R> returnType();

	public R call(Map<Parameter<?>, Value<?>> args) throws Exception;
}
