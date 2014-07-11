package jprobe.system.model.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import jprobe.framework.model.Function;
import jprobe.framework.model.InvocationException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.Procedure;
import jprobe.framework.model.Signature;
import jprobe.framework.model.TypeMismatchException;
import jprobe.system.model.FixedValueFunction;
import jprobe.system.model.FunctionFactory;
import jprobe.system.model.FunctionFactoryImpl;

public class FunctionTest extends junit.framework.TestCase{
	
	private static class Add extends Procedure<Integer>{
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
		public Class<? extends Integer> getReturnType() {
			return Integer.class;
		}
		@Override
		public Integer invoke(Function<?>... args)
				throws IllegalArgumentException, TypeMismatchException,
				InvocationException {
			try{
				int a = (Integer) args[0].invoke();
				int b = (Integer) args[1].invoke();
				return a + b;
			}catch(Exception e){
				throw new InvocationException(e);
			}
		}
	}
	
	private static class Reduce<T> extends Procedure<T>{
		
		private final Parameter<T> functionParam;
		private final Parameter<T> startValue;
		private final Parameter<List<T>> listParam;
		private final Class<? extends T> clazz;
		
		public Reduce(Class<? extends T> clazz){
			this.clazz = clazz;
			functionParam = new StubParameter<T>(
					clazz,
					new Signature<?>[]{
							new StubParameter<T>(clazz),
							new StubParameter<T>(clazz)
							}
					);
			startValue = new StubParameter<T>(clazz);
			listParam = new ListParameter<T>(clazz);
		}
		
		 

		@Override
		public Parameter<?>[] getParameters() {
			return new Parameter<?>[]{functionParam, startValue, listParam};
		}

		@Override
		public T invoke(Function<?>... args) throws IllegalArgumentException,
				TypeMismatchException, InvocationException {
			try{
				T aggregate = (T) args[1].invoke();
				for(T val : (List<T>) args[2].invoke()){
					aggregate = (T) args[0].invoke(
							new FixedValueFunction<T>(aggregate),
							new FixedValueFunction<T>(val));
				}
				return aggregate;
			}catch(Exception e){
				throw new InvocationException(e);
			}
		}

		@Override
		public Class<? extends T> getReturnType() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private static final FunctionFactory FACTORY = new FunctionFactoryImpl();
	private static final Add ADD_PROCEDURE = new Add();
	
	private static int sum(int ... vals){
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
		
		Function<?>[] args = new Function<?>[vals.length];
		for(int i=0; i<vals.length; i++){
			args[i] = new FixedValueFunction<Integer>(vals[i]);
		}
		int	ret0 = add.invoke(args);
		int ret1 = call1.invoke(args[1]);
		int	ret2 = call2.invoke();
		int sum = sum(vals);
		
		assertEquals(sum, ret0);
		assertEquals(sum, ret1);
		assertEquals(sum, ret2);
		
		
	}
	
	
	public void testNested() throws TypeMismatchException, IllegalArgumentException, InvocationException {
		
		Function<Integer> add = FACTORY.newFunction(ADD_PROCEDURE);
		
		int[] vals = new int[]{5, 10, 23, 12, 56, 37};
		Function<?>[] args = new Function<?>[vals.length];
		for(int i=0; i<vals.length; i++){
			args[i] = new FixedValueFunction<Integer>(vals[i]);
		}
		
		int i = 0;
		Function<Integer> call1 = add.putArgument(0, add);
		int sum3 = sum(vals[0], vals[1], vals[2]);
		assertEquals(sum3, (int) call1.invoke(args[0], args[1], args[2]));
		
		Function<Integer> call2 = call1.putArgument(0, vals[i++]);
		assertEquals(sum3, (int) call2.invoke(args[1], args[2]));
		
		Function<Integer> call3 = call2.putArgument(0, vals[i++]);
		assertEquals(sum3, (int) call3.invoke(args[2]));
		
		Function<Integer> call4 = call3.putArgument(0, add);
		int sum4 = sum(sum3, vals[3]);
		assertEquals(sum4, (int) call4.invoke(args[2], args[3]));
		
		Function<Integer> call5 = call4.putArgument(0, add);
		int sum5 = sum(sum4, vals[4]);
		assertEquals(sum5, (int) call5.invoke(args[2], args[3], args[4]));
		
		Function<Integer> call6 = call5.putArgument(0, vals[i++]);
		assertEquals(sum5, (int) call6.invoke(args[3], args[4]));
		
		Function<Integer> call7 = call6.putArgument(0, vals[i++]);
		assertEquals(sum5, (int) call7.invoke(args[4]));
		
		Function<Integer> call8 = call7.putArgument(0, add);
		int sum6 = sum(sum5, vals[5]);
		assertEquals(sum6, (int) call8.invoke(args[4], args[5]));
		
		Function<Integer> call9 = call8.putArgument(0, vals[i++]);
		assertEquals(sum6, (int) call9.invoke(args[5]));
		
		Function<Integer> call10 = call9.putArgument(0, vals[i++]);
		assertEquals(sum6, (int) call10.invoke());
		
		Function<Integer> nested5 = call5.putArgument(1, call5);
		int nested5Sum = sum(vals[0], vals[1], vals[0], vals[1]);
		assertEquals(sum(nested5Sum, vals[2], vals[2], vals[3], vals[3], vals[4]),
				(int) nested5.invoke(args[2], args[2], args[3], args[3], args[4]));
		
	}
	
	public void testDeepNesting() throws TypeMismatchException, IllegalArgumentException, InvocationException{
		int seed = 13;
		Random r = new Random(seed);
		int depth = 2000;
		int params = 2 + depth;
		Function<Integer> add = FACTORY.newFunction(ADD_PROCEDURE);
		Function<Integer> nested = add;
		for(int i=0; i<depth; i++){
			int index = r.nextInt(nested.getParameters().length);
			nested = nested.putArgument(index, add);
		}
		assertEquals(params, nested.getParameters().length);
		
		int sum = 0;
		for(int i=0; i<params; i++){
			sum += i + 1;
			nested = nested.putArgument(0, i + 1);
		}
		
		assertEquals(sum, (int) nested.invoke());
		
	}
	
	public void testFunctionArgument() throws TypeMismatchException, IllegalArgumentException, InvocationException{
		Reduce<Integer> reduceProcedure = new Reduce<Integer>(Integer.class);
		Function<Integer> reduce = FACTORY.newFunction(reduceProcedure);
		Function<Integer> add = FACTORY.newFunction(ADD_PROCEDURE);
		
		Function<Integer> sumList = reduce.putArgument(0, add).putArgument(0, new FixedValueFunction<Integer>(0));
		
		int[] vals = new int[]{5,13,65,18,128,-12,-1235,465,354};
		int sum = sum(vals);
		
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0; i<vals.length; ++i){
			list.add(vals[i]);
		}
		assertEquals(sum, (int) sumList.invoke(new FixedValueFunction<List<Integer>>(list)));
		
		
	}
	
	
	
	
	
}
