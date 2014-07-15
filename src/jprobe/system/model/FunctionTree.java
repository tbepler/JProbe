package jprobe.system.model;

public class FunctionTree {
	
	private static class Node{
		private final Node parent;
		private final Argument[] args;
		
		public Node(Node parent, Argument ... args){
			this.parent = parent;
			this.args = args;
		}
	}
	
}
