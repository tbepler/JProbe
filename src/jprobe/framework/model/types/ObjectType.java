package jprobe.framework.model.types;

import java.util.Deque;

import jprobe.framework.model.function.Procedure;
import jprobe.framework.model.tuple.Tuple;

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
	public T cast(Deque<Object> objs) {
		if(objs == null || objs.size() == 0){
			return null;
		}
		Object obj = objs.poll();
		try{
			return this.cast(obj);
		}catch(RuntimeException e){
			objs.push(obj);
			throw e;
		}
	}
	
	@Override
	public T cast(Object obj){
		if(obj == null){
			return null;
		}
		Type<?> type = Types.typeOf(obj);
		if(this.isTypeInstance(type)){
			return m_Clazz.cast(obj);
		}
		//check if the object can be unwrapped into this type and unwrap it
		return this.unwrap(obj);
	}

	@Override
	public boolean isAssignableFrom(Deque<Type<?>> other) {
		if(other == null || other.size() == 0) return false;
		//retrieve the first type from the array, and check it
		Type<?> type = other.poll();
		if(this.isAssignableFrom(type)){
			return true;
		}
		other.push(type);
		return false;
	}
	
	@Override
	public boolean isAssignableFrom(Type<?> type){
		if(type == null) return false;
		if(type == this) return true;
		if(type instanceof ObjectType){
			ObjectType<?> objType = (ObjectType<?>) type;
			return m_Clazz.isAssignableFrom(objType.m_Clazz);
		}
		//check for unwrapping of single element tuples and no
		//parameter procedures
		return this.canUnwrap(type);
	}
	
	/**
	 * Checks if the type can be unwrapped.
	 * @param type
	 * @return
	 */
	private boolean canUnwrap(Type<?> type){
		if(type instanceof TupleClass){
			return this.canUnwrap((TupleClass) type);
		}
		if(type instanceof Signature){
			return this.canUnwrap((Signature<?>) type);
		}
		return false;
	}
	
	/**
	 * Unwraps the object if it is a tuple or procedure.
	 * @param obj
	 * @return
	 */
	private T unwrap(Object obj){
		Type<?> type = Types.typeOf(obj);
		if(!this.canUnwrap(type)){
			throw new ClassCastException("Object: "+obj+" of type: "+type+" cannot be cast to type: "+this);
		}
		if(obj instanceof Tuple){
			return this.unwrap((Tuple) obj);
		}
		if(obj instanceof Procedure){
			return this.unwrap((Procedure<?>) obj);
		}
		throw new ClassCastException("Object: "+obj+" of type: "+type+" cannot be cast to type: "+this);
	}
	
	/**
	 * A tuple can be unwrapped by this type if it is a single element tuple and
	 * that element can be assigned to this type.
	 * @param type
	 * @return
	 */
	private boolean canUnwrap(TupleClass type){
		if(type.size() == 1){
			return this.isAssignableFrom(type.get(0));
		}
		return false;
	}
	
	/**
	 * Unwraps a single element tuple by extracting the element and casting it
	 * to this type.
	 * @param obj
	 * @return
	 */
	private T unwrap(Tuple obj){
		if(obj.size() == 1){
			return this.cast(obj.get(0));
		}
		throw new ClassCastException("Object: "+obj+" of type: "+obj.getType()+" cannot be cast to type: "+this);
	}
	
	/**
	 * A signature can be unwrapped if it requires no parameters and returns a type
	 * that can be assigned to this type.
	 * @param type
	 * @return
	 */
	private boolean canUnwrap(Signature<?> type){
		if(type.numParameters() == 0){
			return this.isAssignableFrom(type.getReturnType());
		}
		return false;
	}
	
	/**
	 * Unwraps a no params procedure by invoking it and casting the result.
	 * @param proc
	 * @return
	 */
	private T unwrap(Procedure<?> proc){
		if(proc.numParameters() == 0){
			try{
				return this.cast(proc.invoke());
			}catch(Exception e){
				//proceed to throw the class cast exception
			}
		}
		throw new ClassCastException("Object: "+proc+" of type: "+proc.getType()+" cannot be cast to type: "+this);
	}
	
	@Override
	public boolean isTypeInstance(Type<?> other) {
		if(other == null) return false;
		if(other == this) return true;
		if(other instanceof ObjectType){
			ObjectType<?> type = (ObjectType<?>) other;
			return m_Clazz.isAssignableFrom(type.m_Clazz);
		}
		return false;
	}

	@Override
	public boolean isInstance(Object obj) {
		Type<?> type = Types.typeOf(obj);
		return this.isTypeInstance(type);
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
