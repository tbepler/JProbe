package jprobe.framework.model.types;

import util.ArrayUtils;
import jprobe.framework.model.function.Procedure;
import jprobe.framework.model.tuple.Tuple;
import jprobe.framework.model.tuple.Tuple2;

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
	public Tuple2<T,Object[]> cast(Object ... objs) {
		if(objs == null || objs.length == 0){
			return resultTuple(null, objs);
		}
		Object obj = objs[0];
		if(obj == null){
			return resultTuple(null, ArrayUtils.tail(objs));
		}
		Type<?> type = Types.typeOf(obj);
		if(this.isTypeInstance(type)){
			T cast = m_Clazz.cast(obj);
			return resultTuple(cast, ArrayUtils.tail(objs));
		}
		//check if the object can be unwrapped into this type and unwrap it
		T unwrapped = this.unwrap(obj);
		return resultTuple(unwrapped, ArrayUtils.tail(objs));
	}

	@Override
	public Tuple2<Boolean,Type<?>[]> isAssignableFrom(Type<?> ... other) {
		if(other == null || other.length == 0) return resultTuple(false,other);
		//retrieve the first type from the array, and check it
		Type<?> type = other[0];
		if(type == this) return resultTuple(true,other);
		if(type instanceof ObjectType){
			ObjectType<?> objType = (ObjectType<?>) type;
			return resultTuple(m_Clazz.isAssignableFrom(objType.m_Clazz),other);
		}
		//check for unwrapping of single element tuples and no
		//parameter procedures
		return resultTuple(this.canUnwrap(type),other);
	}
	
	private static <T> Tuple2<T,Object[]> resultTuple(T obj, Object ... objs){
		return new Tuple2<T,Object[]>(obj,objs);
	}
	
	private static <T> Tuple2<Boolean,T[]> resultTuple(boolean result, T ...types){
		if(result){
			return new Tuple2<Boolean,T[]>(result,ArrayUtils.tail(types));
		}else{
			return new Tuple2<Boolean,T[]>(result,types);
		}
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
			return this.isAssignableFrom(type.get(0)).first;
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
			return this.cast(obj.get(0)).first;
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
			return this.isAssignableFrom(type.getReturnType()).first;
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
				return this.cast(proc.invoke()).first;
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
