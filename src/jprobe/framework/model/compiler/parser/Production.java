package jprobe.framework.model.compiler.parser;

public interface Production<S> {
	
	public S leftHandSide();
	
	public S[] rightHandSide();
	
}
