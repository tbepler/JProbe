package jprobe.framework.model.types;

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
	public T cast(Object obj) {
		if(obj == null){
			return null;
		}
		if(this.isInstance(obj)){
			return m_Clazz.cast(obj);
		}
		throw new ClassCastException("Object: "+obj+" of type: "+Types.typeOf(obj)+" cannot be cast to type: "+this);
	}

	@Override
	public boolean isAssignableFrom(Type<?> other) {
		if(other == null) return false;
		if(other == this) return true;
		if(other instanceof ObjectType){
			ObjectType<?> type = (ObjectType<?>) other;
			return m_Clazz.isAssignableFrom(type.m_Clazz);
		}
		if(other instanceof TupleClass){
			TupleClass<?> type = (TupleClass<?>) other;
			//check if the tuple can be unwrapped into this type
			if(type.size() == 1){
				return this.isAssignableFrom(type.get(0));
			}
		}
		if(other instanceof Signature){
			Signature<?> type = (Signature<?>) other;
			//check if the procedure can be unwrapped into this type
			if(type.numParameters() == 0){
				return this.isAssignableFrom(type.getReturnType());
			}
		}
		return false;
	}

	@Override
	public boolean isInstance(Object obj) {
		Type<?> type = Types.typeOf(obj);
		return this.isAssignableFrom(type);
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

}
