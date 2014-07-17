package jprobe.framework.model.compiler;

public interface SyntaxElement {
	
	public boolean isStart();
	
	public int priority();
	
	public int size();
	
	public SyntaxElement getChild(int index);
	
	public SyntaxElement setChild(int index);
	
}
