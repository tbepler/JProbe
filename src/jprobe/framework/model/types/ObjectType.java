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
	public T extract(Deque<Object> objs) {
		if(objs == null || objs.size() == 0){
			return null;
		}
		Object obj = objs.poll();
		try{
			return this.extract(obj);
		}catch(RuntimeException e){
			objs.push(obj);
			throw e;
		}
	}
	
	public T extract(Object obj){
		if(this.isInstance(obj)){
			return this.cast(obj);
		}
		return this.unwrap(obj);
	}

	@Override
	public boolean isExtractableFrom(Deque<Type<?>> other) {
		if(other == null || other.size() == 0) return false;
		//retrieve the first type from the array, and check it
		Type<?> type = other.poll();
		if(this.isExtractableFrom(type)){
			return true;
		}
		other.push(type);
		return false;
	}
	
	public boolean isExtractableFrom(Type<?> type){
		if(this.isAssignableFrom(type)){
			return true;
		}
		//check for unwrapping of single element tuples and no
		//parameter procedures
		return this.canUnwrap(type);
	}

	@Override
	public boolean canExtract(Deque<Object> objs) {
		Deque<Type<?>> types = Types.typesOf(objs);
		return this.isExtractableFrom(types);
	}
	
	@Override
	public T cast(Object obj){
		if(obj == null){
			return null;
		}
		Type<?> type = Types.typeOf(obj);
		if(this.isAssignableFrom(type)){
			return m_Clazz.cast(obj);
		}
		throw new ClassCastException("Object: "+obj+" of type: "+type+" cannot be cast to type: "+this);
	}
	
	@Override
	public boolean isAssignableFrom(Type<?> type){
		if(type == null) return false;
		if(type == this) return true;
		if(type instanceof ObjectType){
			ObjectType<?> objType = (ObjectType<?>) type;
			return m_Clazz.isAssignableFrom(objType.m_Clazz);
		}
		return false;
	}

	@Override
	public boolean isInstance(Object obj) {
		Type<?> type = Types.typeOf(obj);
		return this.isAssignableFrom(type);
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
		return type.size() == 1 && this.isExtractableFrom(type.get(0));
	}
	
	/**
	 * Unwraps a single element tuple by extracting the element and casting it
	 * to this type.
	 * @param obj
	 * @return
	 */
	private T unwrap(Tuple obj){
		if(obj.size() == 1){
			return this.extract(obj.get(0));
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
		return type.numParameters() == 0 && this.isExtractableFrom(type.getReturnType());
	}
	
	/**
	 * Unwraps a no params procedure by invoking it and casting the result.
	 * @param proc
	 * @return
	 */
	private T unwrap(Procedure<?> proc){
		if(proc.numParameters() == 0){
			try{
				return this.extract(proc.invoke());
			}catch(Exception e){
				//proceed to throw the class cast exception
			}
		}
		throw new ClassCastException("Object: "+proc+" of type: "+proc.getType()+" cannot be cast to type: "+this);
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
