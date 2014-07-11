package jprobe.system.model;

import java.util.ArrayList;
import java.util.List;

import util.tuple.Tuple2;
import jprobe.framework.model.Function;
import jprobe.framework.model.Procedure;
import jprobe.framework.model.TypeMismatchException;

public class FunctionFactoryImpl implements FunctionFactory{

	@Override
	public <T> Function<T> newFunction(Procedure<T> procedure) {
		return new RootFunction<T>(this, procedure);
	}

	@Override
	public <T> Function<T> newFixedValueFunction(T value) {
		return new FixedValueFunction<T>(value);
	}

	@Override
	public <T> Function<T> newNullValueFunction(Class<T> clazz) {
		return new FixedValueFunction<T>(clazz);
	}

	@Override
	public <T, U> Function<T> newFunction(Function<T> parent, int paramIndex,
			Function<U> arg) throws TypeMismatchException {
		return this.newFunction(parent, new int[]{paramIndex}, new Function<?>[]{arg});
	}

	@Override
	public <T, U> Function<T> newFunction(Function<T> parent, int paramIndex,
			U arg) throws TypeMismatchException {
		return this.newFunction(parent, new int[]{paramIndex}, new Object[]{arg});
	}

	@Override
	public <T> Function<T> newFunction(Function<T> parent, int[] indices,
			Function<?>[] args) throws TypeMismatchException, IllegalArgumentException {
		List<Tuple2<Integer, Function<?>>> list = new ArrayList<Tuple2<Integer, Function<?>>>();
		if((indices == null || indices.length == 0) && (args == null || args.length == 0)){
			return new ChildFunction<T>(this, parent, list);
		}
		if(indices == null || args == null || indices.length != args.length){
			throw new IllegalArgumentException("Indices array and arguments array must be of same length."
					+ " Indices length: "+(indices == null ? 0 : indices.length) + " args length: "
					+ (args == null ? 0 : args.length) );
		}
		//check types
		//for(int i=0; i<args.length; i++){
		//	Parameters.checkType(parent.getParameters()[indices[i]], args[i]);
		//}
		//allocate tuples
		for(int i=0; i<args.length; i++){
			list.add(new Tuple2<Integer, Function<?>>(indices[i], args[i]));
		}
		return new ChildFunction<T>(this, parent, list);
	}

	@Override
	public <T> Function<T> newFunction(Function<T> parent, int[] indices,
			Object[] args) throws TypeMismatchException, IllegalArgumentException{
		Function<?>[] functions = new Function<?>[args.length];
		for(int i=0; i<args.length; i++){
			functions[i] = this.newFixedValueFunction(args[i]);
		}
		return this.newFunction(parent, indices, functions);
		
	}



}
