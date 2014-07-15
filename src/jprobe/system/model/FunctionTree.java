package jprobe.system.model;

import java.util.Collection;

import jprobe.framework.model.types.Type;

public class FunctionTree {
	
	private static class Node{
		private final Node parent;
		private final Argument[] args;
		
		public Node(Node parent, Argument ... args){
			this.parent = parent;
			this.args = args;
		}
	}
	
	private static void resolve(Type<?> target, Type<?> received, Collection<Type<?>> params){
		if(!target.isExtractableFrom(received)){
			//try to expand params with params from received, if received is a function
			
		}
	}
	
}
