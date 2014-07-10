package jprobe.system.model.test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import jprobe.framework.model.Function;
import jprobe.framework.model.InvocationException;
import jprobe.framework.model.MissingArgumentsException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.Procedure;
import jprobe.framework.model.TypeMismatchException;
import jprobe.framework.model.Value;
import jprobe.system.model.RootFunction;

public class FunctionTest extends junit.framework.TestCase{
	
	private static class Add implements Procedure<Integer>{
		private static final long serialVersionUID = 1L;
		
		private static final Parameter<?>[] PARAMS = new Parameter<?>[]{
			new StubParameter<Integer>(Integer.class),
			new StubParameter<Integer>(Integer.class)
		};
		
		@Override
		public Parameter<?>[] getParameters() {
			return PARAMS;
		}
		@Override
		public Class<? extends Integer> returnType() {
			return Integer.class;
		}
		@Override
		public Integer invoke(Value<?>... args)
				throws IllegalArgumentException, TypeMismatchException,
				InvocationException {
			try{
				int a = (Integer) args[0].get();
				int b = (Integer) args[1].get();
				return a + b;
			}catch(Exception e){
				throw new InvocationException(e);
			}
		}
		
		
		
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
