package jprobe.system.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jprobe.framework.model.function.Function;
import jprobe.framework.model.function.Parameter;
import jprobe.framework.model.types.Signature;

public class ParameterTree implements Serializable{
	
	private static class Node implements Serializable{
		
		private final Function<?> m_Func;
		private final List<Definition> m_Defs;
		
		public Node(Function<?> func, Signature<?> sig){
			m_Func = func;
			m_Defs = new ArrayList<Definition>();
			if(m_Func != null){
				if(!sig.subsignatureOf(func)){
					throw new TypeMismatchException()
				}
				List<Integer> undefinedIndices = Signature.getUnassignableSignatureIndices(func.getParameters(), sig.getParameters());
			}
		}
		
	}
	
	private static class Definition implements Serializable{
		public int index;
		public Parameter<?> parameter;
		public Node node;
		
		public Definition(int index, Parameter<?> param){
			this.index = index;
			this.parameter = param;
			this.node = null;
		}
		
	}
	
}
