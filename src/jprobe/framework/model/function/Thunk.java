package jprobe.framework.model.function;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import jprobe.framework.model.Pointer;
import jprobe.framework.model.types.Type;

public class Thunk implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private enum Instructions{
		ASSIGN,
		BOX,
		UNBOX;
	}
	
	private final Function<?,?> m_Fun;
	private final Deque<Pointer> m_Args;
	private final Deque<Instructions> m_Instrs;
	private final Type<?> m_Type;
	
	public Thunk(Function<?,?> fun, Deque<Pointer> args) throws IllegalArgumentException, TypeMismatchException{
		m_Fun = fun;
		m_Args = args;
		m_Instrs = new LinkedList<Instructions>();
		//type checking here
		m_Type = this.checkTypes();
	}
	
	private Type<?> checkTypes() throws TypeMismatchException, IllegalArgumentException{
		Signature<?,?> sign = m_Fun.getType();
		Deque<Type<?>> types = argTypes();
		return this.assignParameters(sign, m_Instrs, types);
	}
	
	private Type<?> assignParameters(Type<?> cur, Deque<Instructions> instrs, Deque<Type<?>> types)
			throws TypeMismatchException, IllegalArgumentException{
		if(cur instanceof Signature){
			Signature<?,?> sign = (Signature<?,?>) cur;
			return assignParameters(sign, instrs, types);
		}
		if(types.isEmpty()){
			return cur;
		}
		StringBuilder builder = new StringBuilder("Unable to apply types: (");
		boolean first = true;
		for(Type<?> t : types){
			if(first){
				builder.append(t);
				first = false;
			}else{
				builder.append(", ").append(t);
			}
		}
		builder.append(") to type: ").append(cur);
		throw new IllegalArgumentException(builder.toString());
	}
	
	private Type<?> assignParameters(Signature<?,?> sign, Deque<Instructions> instrs, Deque<Type<?>> types)
			throws TypeMismatchException, IllegalArgumentException{
		if(types.isEmpty()){
			return sign;
		}
		Type<?> paramType = sign.getParameterType();
		Type<?> argType = types.peek();
		
		//check for assignment of the argument type
		//to the parameter type
		if(this.assign(paramType, argType, instrs)){
			return assignParameters(sign.getReturnType(), instrs, types);
		}
		//the argType is not assignable, throw an error
		throw new TypeMismatchException("Expected parameter type: "+paramType+" cannot be assigned from type: "+argType);
	}
	
	private boolean assign(Type<?> param, Type<?> arg, Deque<Instructions> instrs){
		if(param.isAssignableFrom(arg)){
			instrs.add(Instructions.ASSIGN);
			return true;
		}
		if(box(param, arg, instrs)){
			return true;
		}
		if(unbox(param, arg, instrs)){
			return true;
		}
		return false;
	}
	
	private boolean box(Type<?> param, Type<?> arg, Deque<Instructions> instrs){
		//only box single element types
		if(param.isBoxable() && param.size() == 1){
			instrs.addLast(Instructions.BOX);
			Type<?> unbox = param.unbox()[0];
			if(this.assign(unbox, arg, instrs)){
				return true;
			}
			instrs.removeLast();
		}
		return false;
	}
	
	private boolean unbox(Type<?> param, Type<?> arg, Deque<Instructions> instrs){
		//only unbox single element types
		if(arg.isBoxable() && arg.size() == 1){
			instrs.addLast(Instructions.UNBOX);
			Type<?> unbox = arg.unbox()[0];
			if(this.assign(param, unbox, instrs)){
				return true;
			}
			instrs.removeLast();
		}
		return false;
	}
	
	private Deque<Type<?>> argTypes(){
		Deque<Type<?>> types = new LinkedList<Type<?>>();
		for(Pointer p : m_Args){
			types.add(p.getReferenceType());
		}
		return types;
	}
	
	public Type<?> getEvaluationType() {
		return m_Type;
	}
	
	public Object evaluate(){
		//TODO
		return null;
	}


	
	
	
}
