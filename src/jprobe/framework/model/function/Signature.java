package jprobe.framework.model.function;

import java.util.Arrays;
import java.util.Deque;

import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.TypeCastException;
import jprobe.framework.model.types.Types;

public final class Signature<P,R> implements Type<Function<P,R>>{
	private static final long serialVersionUID = 1L;
	
	private final Type<P> m_Param;
	private final Type<R> m_ReturnType;
	private final int m_Hash;
	
	public Signature(Type<P> param, Type<R> returnType){
		m_ReturnType = returnType;
		m_Param = param;
		m_Hash = this.computeHash();
	}
	
	private int computeHash(){
		return Arrays.hashCode(new Object[]{m_Param, m_ReturnType});
	}
	
	public Type<P> getParameterType(){
		return m_Param;
	}
	
	public Type<R> getReturnType(){
		return m_ReturnType;
	}
	
	@Override
	public String toString(){
		return m_Param + " -> " + m_ReturnType;
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Signature){
			Signature<?,?> other = (Signature<?,?>) o;
			return m_Param.equals(other.m_Param) && m_ReturnType.equals(other.m_ReturnType);
		}
		return false;
	}
	
	@Override
	public boolean isAssignableFrom(Type<?> t){
		if(t == null) return false;
		if(t == this) return true;
		if(t instanceof Signature){
			Signature<?,?> other = (Signature<?,?>) t;
			return other.m_Param.isAssignableFrom(m_Param) && m_ReturnType.isAssignableFrom(other.m_ReturnType);
		}
		return false;
	}
	
	@Override
	public boolean isInstance(Object o){
		if(o == null) return false;
		Type<?> type = Types.typeOf(o);
		return this.isAssignableFrom(type);
	}
	
	@Override
	public Function<P,R> cast(Object o){
		if(o == null) return null;
		if(o instanceof Function){
			Function<?,?> other = (Function<?,?>) o;
			if(this.isInstance(other)){
				return (Function<P, R>) other;
			}
		}
		throw new TypeCastException("Object: "+o+" of type: "+Types.typeOf(o)+" cannot be cast to type: "+this);
	}

	@Override
	public boolean isBoxable() {
		return false;
	}

	@Override
	public Type<?>[] unbox() {
		return null;
	}

	@Override
	public Object[] unbox(Function<P,R> obj) {
		return null;
	}

	@Override
	public boolean canBox(Deque<Type<?>> types) {
		return false;
	}

	@Override
	public Function<P,R> box(Deque<Object> objs) {
		return null;
	}
	
	
	
	
	
}