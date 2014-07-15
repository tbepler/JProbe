package jprobe.framework.model.types;

import java.util.Deque;

public interface BoxableType<T> extends Type<T> {
	
	public Deque<Type<?>> unbox();
	public Deque<Object> unbox(Object obj);
	public T box(Deque<Object> objs);
	
	public boolean isVarArgsType();
	public VarArgsType<T> asVarArgsType();

}
