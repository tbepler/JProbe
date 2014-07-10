package jprobe.framework.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface Procedure<R> extends Serializable{
	
	public List<? extends Parameter<?>> getParameters();
	
	public Class<R> returnType();

	public R call(Map<Parameter<?>, Value<?>> args) throws Exception;
}
