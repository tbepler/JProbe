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
import jprobe.system.model.FixedValue;
import jprobe.system.model.FunctionFactory;
import jprobe.system.model.FunctionFactoryImpl;
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
	
	private static final FunctionFactory FACTORY = new FunctionFactoryImpl();
	private static final Add ADD_PROCEDURE = new Add();
	
	private static int sum(int[] vals){
		int sum = 0;
		for(int i : vals){
			sum += i;
		}
		return sum;
	}
	
	public void testSimple() throws IllegalArgumentException, TypeMismatchException, InvocationException{
		
		Function<Integer> add = FACTORY.newFunction(ADD_PROCEDURE);
		
		int[] vals = new int[]{5, 23};
		
		int count = 0;
		Function<Integer> call1 = add.putArgument(0, vals[count++]);
		Function<Integer> call2 = call1.putArgument(0, vals[count++]);
		
		Value<?>[] args = new Value<?>[vals.length];
		for(int i=0; i<vals.length; i++){
			args[i] = new FixedValue<Integer>(vals[i]);
		}
		int	ret0 = add.invoke(args);
		int ret1 = call1.invoke(args[1]);
		int	ret2 = call2.invoke(null);
		int sum = sum(vals);
		
		assertEquals(sum, ret0);
		assertEquals(sum, ret1);
		assertEquals(sum, ret2);
		
		
	}
	
	/*
	public void testNested() throws MissingArgumentsException, ExecutionException{
		
		int[] vals = new int[]{5, 10, 23, 12, 56, 37};
		
		int sum = 0;
		int i = 0;
		
		
		
	}
	*/
}
