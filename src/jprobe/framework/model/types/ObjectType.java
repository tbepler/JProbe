package jprobe.framework.model.types;

import java.util.Deque;

public class ObjectType<T> implements Type<T> {
	private static final long serialVersionUID = 1L;
	
	private final Class<? extends T> m_Clazz;
	
	ObjectType(Class<? extends T> clazz) {
		m_Clazz = clazz;
	}
	
	public Class<? extends T> getClassType(){
		return m_Clazz;
	}
	
	@Override
	public String toString(){
		return String.valueOf(m_Clazz);
	}
	
	@Override
	public int hashCode(){
		return m_Clazz.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof ObjectType){
			ObjectType<?> type = (ObjectType<?>) o;
			return m_Clazz.equals(type.m_Clazz);
		}
		return false;
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
	public Object[] unbox(T obj) {
		return null;
	}

	@Override
	public boolean canBox(Deque<Type<?>> types) {
		return false;
	}

	@Override
	public T box(Deque<Object> objs) {
		return null;
	}

	@Override
	public T cast(Object obj) {
		return m_Clazz.cast(obj);
	}

	@Override
	public boolean isAssignableFrom(Type<?> type) {
		if(type == null) return false;
		if(type == this) return true;
		if(type instanceof ObjectType){
			ObjectType<?> other = (ObjectType<?>) type;
			return m_Clazz.isAssignableFrom(other.m_Clazz);
		}
		return false;
	}

	@Override
	public boolean isInstance(Object obj) {
		if(obj == null) return false;
		Type<?> type = Types.typeOf(obj);
		return this.isAssignableFrom(type);
	}

}
