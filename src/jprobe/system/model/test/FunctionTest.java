package jprobe.system.model.test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import jprobe.framework.model.Function;
import jprobe.framework.model.MissingArgumentsException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.Procedure;
import jprobe.framework.model.Value;
import jprobe.system.model.RootFunction;

public class FunctionTest extends junit.framework.TestCase{
	
	private static class Add implements Procedure<Integer>{
		
		private final Parameter<Integer> x = new StubParameter<Integer>(Integer.class);
		private final Parameter<Integer> y = new StubParameter<Integer>(Integer.class);
		
		@SuppressWarnings("unchecked")
		@Override
		public List<Parameter<Integer>> getParameters() {
			return Arrays.asList(x,y);
		}

		@Override
		public Class<Integer> returnType() {
			return Integer.class;
		}
		
		private <T> Value<T> getValue(Parameter<T> param, Map<Parameter<?>, Value<?>> args){
			return (Value<T>) args.get(param);
		}

		@Override
		public Integer call(Map<Parameter<?>, Value<?>> args) throws Exception {
			Value<Integer> a = this.getValue(x, args);
			Value<Integer> b = this.getValue(y, args);
			return a.get() + b.get();
		}
		
	}
	
	private <R,T> Function<R> putValue(Function<R> func, Parameter<T> param, final T value){
		return func.putArgument(param, new StubFunction<T>((Class<T>)value.getClass()){

			@Override
			public T call(Map<Parameter<?>, Value<?>> args) throws MissingArgumentsException, ExecutionException {
				return value;
			}
			
		});
	}
	
	public void testSimple(){
		
		Function<Integer> add = new RootFunction<Integer>(new Add());
		
		int a = 5;
		int b = 31;
		
		Function<Integer> call = add;
		int count = 0;
		for(Parameter<?> param : add.getParameters()){
			int val = count == 0 ? a : b;
			call = putValue(call, (Parameter<Integer>)param, val);
			++count;
		}
		
		int ret;
		try {
			ret = call.call();
		} catch (MissingArgumentsException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
		
		assertEquals(a+b, ret);
		
	}
	
	public void testNested() throws MissingArgumentsException, ExecutionException{
		Function<Integer> add = new RootFunction<Integer>(new Add());
		
		int[] vals = new int[]{5, 10, 23, 12, 56, 37};
		Function<Integer> call = add;
		
		int sum = 0;
		int i = 0;
		
		assertEquals(2, call.getParameters().size());
		call = call.putArgument((Parameter<Integer>)call.getParameters().get(0), add);
		assertEquals(3, call.getParameters().size());
		
		call = putValue(call, (Parameter<Integer>)call.getParameters().get(0), vals[i]);
		assertEquals(2, call.getParameters().size());
		sum += vals[i++];
		call = putValue(call, (Parameter<Integer>)call.getParameters().get(0), vals[i]);
		assertEquals(1, call.getParameters().size());
		sum += vals[i++];
		
		call = call.putArgument((Parameter<Integer>)call.getParameters().get(0), add);
		assertEquals(2, call.getParameters().size());
		call = call.putArgument((Parameter<Integer>)call.getParameters().get(0), add);
		assertEquals(3, call.getParameters().size());
		
		call = putValue(call, (Parameter<Integer>)call.getParameters().get(0), vals[i]);
		assertEquals(2, call.getParameters().size());
		sum += vals[i++];
		call = putValue(call, (Parameter<Integer>)call.getParameters().get(0), vals[i]);
		assertEquals(1, call.getParameters().size());
		sum += vals[i++];
		
		call = call.putArgument((Parameter<Integer>)call.getParameters().get(0), add);
		assertEquals(2, call.getParameters().size());
		
		call = putValue(call, (Parameter<Integer>)call.getParameters().get(0), vals[i]);
		assertEquals(1, call.getParameters().size());
		sum += vals[i++];
		call = putValue(call, (Parameter<Integer>)call.getParameters().get(0), vals[i]);
		assertEquals(0	, call.getParameters().size());
		sum += vals[i++];
		
				
		int ret = call.call();
		assertEquals(sum, ret);
		
		
	}
	
}
