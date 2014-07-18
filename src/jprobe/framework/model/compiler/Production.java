package jprobe.framework.model.compiler;

public interface Production<S> {
	
	public S leftHandSide();
	
	public S[] rightHandSide();
	
}
