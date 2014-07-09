package jprobe.system.model;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import jprobe.framework.model.Function;
import jprobe.framework.model.MissingArgsException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.Value;

public interface TreeFunction<R> extends Function<R>{

	public R call(Map<Parameter<?>, Value<?>> args) throws MissingArgsException, ExecutionException;

}
