package jprobe.framework.model.function;

import java.io.Serializable;

import jprobe.framework.model.Pointer;
import jprobe.framework.model.types.Typed;


public interface Function<P,R> extends Typed<Function<P,R>>, Serializable{
	
	@Override
	public Signature<P,R> getType();
	
	public R apply(Pointer argument);
	

}
