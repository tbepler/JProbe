package jprobe.services.function.test;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;

import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import jprobe.services.function.ArgumentListener;
import jprobe.services.function.Function;

public class Test extends junit.framework.TestCase{
	
	public class TestParameter{
		public String VALUE_A;
		public int VALUE_B;
		public double VALUE_C;
		
		@Override
		public boolean equals(Object o){
			if(o == null) return false;
			if(o == this) return true;
			if(o instanceof TestParameter){
				TestParameter p = (TestParameter) o;
				return this.VALUE_A.equals(p.VALUE_A) && this.VALUE_B == p.VALUE_B && this.VALUE_C == p.VALUE_C;
			}
			return false;
		}
	}
	
	public class TestFunction implements Function<TestParameter>{

		@Override
		public String getName() { return "TestFunction"; }

		@Override
		public String getDescription() { return "A test function"; }

		@Override
		public String getCategory() { return "Test"; }

		@Override
		public TestParameter newParameters() { return new TestParameter(); }

		@Override
		public Collection<Argument<? super TestParameter>> getArguments() {
			Collection<Argument<? super TestParameter>> args = new ArrayList<Argument<? super TestParameter>>();
			args.add(new StringArg());
			args.add(new IntArg());
			args.add(new DoubleArg());
			return args;
		}

		@Override
		public Data execute(ProgressListener l, TestParameter params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public class StringArg implements Argument<TestParameter>{

		@Override
		public String getName() { return "StringArg"; }

		@Override
		public String getTooltip() { return "A string arg"; }

		@Override
		public String getCategory() { return "Test"; }

		@Override
		public boolean isOptional() { return false; }

		@Override
		public boolean isValid() { return true; }

		@Override
		public void addListener(ArgumentListener l) {}

		@Override
		public void removeListener(ArgumentListener l) {}

		@Override
		public JComponent getComponent() { return null; }

		@Override
		public void process(TestParameter params) {
			params.VALUE_A = "String";
		}
		
	}
	
	public class IntArg implements Argument<TestParameter>{

		@Override
		public String getName() { return this.getClass().getSimpleName(); }

		@Override
		public String getTooltip() { return "An int arg"; }

		@Override
		public String getCategory() { return "Test"; }

		@Override
		public boolean isOptional() { return false; }

		@Override
		public boolean isValid() { return true; }

		@Override
		public void addListener(ArgumentListener l) {}

		@Override
		public void removeListener(ArgumentListener l) {}

		@Override
		public JComponent getComponent() { return null; }

		@Override
		public void process(TestParameter params) {
			params.VALUE_B = 5;
		}
		
	}
	
	public class DoubleArg implements Argument<TestParameter>{

		@Override
		public String getName() { return this.getClass().getSimpleName(); }

		@Override
		public String getTooltip() { return "A double arg"; }

		@Override
		public String getCategory() { return "Test"; }

		@Override
		public boolean isOptional() { return false; }

		@Override
		public boolean isValid() { return true; }

		@Override
		public void addListener(ArgumentListener l) {}

		@Override
		public void removeListener(ArgumentListener l) {}

		@Override
		public JComponent getComponent() { return null; }

		@Override
		public void process(TestParameter params) {
			params.VALUE_C = 15.3;
		}
		
	}
	
	public void testGeneric(){
		
		TestParameter compareTrue = new TestParameter();
		compareTrue.VALUE_A = "String";
		compareTrue.VALUE_B = 5;
		compareTrue.VALUE_C = 15.3;
		assertTrue(process(new TestFunction(), compareTrue));
		
		TestParameter compareFalse = new TestParameter();
		compareFalse.VALUE_A = "test";
		compareFalse.VALUE_B = 5;
		compareFalse.VALUE_C = 3.3;
		assertFalse(process(new TestFunction(), compareFalse));
	}
	
	private static <T> boolean process(Function<T> fun, Object compare){
		T params = fun.newParameters();
		for(Argument<? super T> arg : fun.getArguments()){
			arg.process(params);
		}
		return params.equals(compare);
	}
	
}
