package jprobe.framework.model.types;

import java.util.Arrays;
import java.util.Deque;

import jprobe.framework.model.function.Procedure;

public final class Signature implements Type<Procedure<?>>{
	private static final long serialVersionUID = 1L;
	
	private final Type<?> m_Param;
	private final Type<?> m_ReturnType;
	private final int m_Hash;
	
	public Signature(Type<?> param, Type<?> returnType){
		m_ReturnType = returnType;
		m_Param = param;
		m_Hash = this.computeHash();
	}
	
	private int computeHash(){
		return Arrays.hashCode(new Object[]{m_Param, m_ReturnType});
	}
	
	public Type<?> getParameterType(){
		return m_Param;
	}
	
	public Type<?> getReturnType(){
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
			Signature other = (Signature) o;
			return m_Param.equals(other.m_Param) && m_ReturnType.equals(other.m_ReturnType);
		}
		return false;
	}
	
	@Override
	public boolean isAssignableFrom(Type<?> t){
		if(t == null) return false;
		if(t == this) return true;
		if(t instanceof Signature){
			Signature other = (Signature) t;
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
	public Procedure<?> cast(Object o){
		if(o == null) return null;
		if(o instanceof Procedure){
			Procedure<?> other = (Procedure<?>) o;
			if(this.isInstance(other)){
				return other;
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
	public Object[] unbox(Procedure<?> obj) {
		return null;
	}

	@Override
	public boolean canBox(Deque<Type<?>> types) {
		return false;
	}

	@Override
	public Procedure<?> box(Deque<Object> objs) {
		return null;
	}
	
	
	
	
	
}