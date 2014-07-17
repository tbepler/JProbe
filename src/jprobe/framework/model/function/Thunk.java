package jprobe.framework.model.function;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.Typed;

public class Thunk implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private enum Instructions{
		ASSIGN,
		BOX,
		UNBOX;
	}
	
	private final Function<?,?> m_Fun;
	private final Deque<Pointer<?>> m_Args;
	private final Deque<Instructions> m_Instrs;
	private final Type<?> m_Type;
	
	public Thunk(Function<?,?> fun, Deque<Pointer<?>> args) throws IllegalArgumentException, TypeMismatchException{
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
		
		//check for direct assignment of the argument type
		//to the parameter type
		if(paramType.isAssignableFrom(argType)){
			types.poll();
			instrs.add(Instructions.ASSIGN);
			return assignParameters(sign.getReturnType(), instrs, types);
		}
		//check for boxing of the argument types into the 
		//parameter type
		if(paramType.isBoxable() && paramType.canBox(types)){
			instrs.add(Instructions.BOX);
			return assignParameters(sign.getReturnType(), instrs, types);
		}
		//check for unboxing of the argument
		if(argType.isBoxable()){
			types.poll();
			Type<?>[] unboxed = argType.unbox();
			for(int i=unboxed.length-1; i>=0; --i){
				types.push(unboxed[i]);
			}
			instrs.add(Instructions.UNBOX);
			return assignParameters(sign, instrs, types);
		}
		//the argType is not assignable, throw an error
		throw new TypeMismatchException("Expected parameter type: "+paramType+" but was type: "+argType);
	}
	
	private Deque<Type<?>> argTypes(){
		Deque<Type<?>> types = new LinkedList<Type<?>>();
		for(Pointer<?> p : m_Args){
			types.add(p.getPointerType());
		}
		return types;
	}
	
	public Type<?> getEvaluationType() {
		return m_Type;
	}
	
	public Object evaluate(){
		
	}
	
	
	
}
