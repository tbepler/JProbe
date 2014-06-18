package jprobe.services.function.test;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;
import org.osgi.framework.Bundle;

import util.progress.ProgressListener;
import jprobe.ParsingEngine;
import jprobe.services.CoreListener;
import jprobe.services.FunctionManager;
import jprobe.services.data.AbstractFinalData;
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
		public String getName() { return "test"; }

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
			return new TestData(params.VALUE_A, params.VALUE_B, params.VALUE_C);
		}
		
	}
	
	public class TestData extends AbstractFinalData{
		private static final long serialVersionUID = 1L;
		
		public final String S;
		public final double D;
		public final int I;
		
		public TestData(String s, int i, double d){
			super(3, 1);
			S = s; D = d; I = i;
		}
		
		@Override
		public boolean equals(Object o){
			if(o == null) return false;
			if(o == this) return true;
			if(o instanceof TestData){
				TestData d = (TestData) o;
				return d.S.equals(this.S) && d.D == this.D && d.I == this.I;
			}
			return false;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch(columnIndex){
			case 0: return String.class;
			case 1: return Double.class;
			case 2: return Integer.class;
			default: return null;
			}
		}

		@Override
		public String getColumnName(int columnIndex) {
			switch(columnIndex){
			case 0: return "String";
			case 1: return "Double";
			case 2: return "Integer";
			default: return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch(columnIndex){
			case 0: return S;
			case 1: return D;
			case 2: return I;
			default: return null;
			}
		}

		@Override
		public void dispose() {
			//do nothing
		}
		
	}
	
	public class StringArg implements Argument<TestParameter>{

		@Override
		public String getName() { return "string"; }

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

		@Override
		public Character shortFlag() { return 's'; }

		@Override
		public String getPrototypeValue() { return "STRING"; }

		@Override
		public void parse(ProgressListener l, TestParameter params, String[] args) {
			if(args.length < 1){
				throw new RuntimeException("string requires an argument");
			}
			params.VALUE_A = args[0];
		}
		
	}
	
	public class IntArg implements Argument<TestParameter>{

		@Override
		public String getName() { return "int"; }

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

		@Override
		public Character shortFlag() { return 'i'; }

		@Override
		public String getPrototypeValue() { return "INT"; }

		@Override
		public void parse(ProgressListener l, TestParameter params, String[] args) {
			params.VALUE_B = Integer.parseInt(args[0]);
		}
		
	}
	
	public class DoubleArg implements Argument<TestParameter>{

		@Override
		public String getName() { return "double"; }

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

		@Override
		public Character shortFlag() { return 'd'; }

		@Override
		public String getPrototypeValue() { return "DOUBLE"; }

		@Override
		public void parse(ProgressListener l, TestParameter params, String[] args) {
			params.VALUE_C = Double.parseDouble(args[0]);
		}
		
	}
	
	private class ShellFunctionManager implements FunctionManager{

		@Override
		public void addListener(CoreListener listener) {
			//do nothing
		}

		@Override
		public void removeListener(CoreListener listener) {
			//do nothing
		}

		@Override
		public void addFunction(Function<?> f, Bundle responsible) {
			//do nothing
		}

		@Override
		public void removeFunction(Function<?> f, Bundle responsible) {
			//do nothing
		}

		@Override
		public Function<?>[] getAllFunctions() {
			return new Function<?>[]{new TestFunction()};
		}

		@Override
		public Function<?>[] getFunctions(String name) {
			return new Function<?>[]{new TestFunction()};
		}

		@Override
		public String[] getFunctionNames() {
			return new String[]{new TestFunction().getName()};
		}

		@Override
		public Bundle getProvider(Function<?> f) {
			return null;
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
	

	
	public void testParse(){
		
		FunctionManager shell = new ShellFunctionManager();
		String[] args = new String[]{"test", "-s", "text", "-i", "5", "-d", "6.7"};
		TestData correct = new TestData("text", 5, 6.7);
		Data result = ParsingEngine.parseAndExecute(System.err, shell, args);
		assertEquals(correct, result);

		args = new String[]{"test", "--string", "text", "--int", "5", "--double", "6.7"};
		correct = new TestData("text", 5, 6.7);
		result = ParsingEngine.parseAndExecute(System.err, shell, args);
		assertEquals(correct, result);
		
		args = new String[]{"test", "--string", "text", "--int", "--double", "6.7"};
		correct = null;
		result = ParsingEngine.parseAndExecute(System.err, shell, args);
		assertEquals(correct, result);
		
		args = new String[]{"test", "--string", "text", "--int", "5"};
		correct = null;
		result = ParsingEngine.parseAndExecute(System.err, shell, args);
		assertEquals(correct, result);
		
		args = new String[]{"test", "--string", "text", "-h", "--int", "5", "--double", "6.7"};
		correct = null;
		result = ParsingEngine.parseAndExecute(System.err, shell, args);
		assertEquals(correct, result);
	}
	
	
	
	
}
