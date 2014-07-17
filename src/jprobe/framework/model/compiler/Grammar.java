package jprobe.framework.model.compiler;

import java.util.Deque;

public interface Grammar {
	
	public char statementTerminator();
	
	public boolean matches(Deque<Element> elems);
	
	public void reduce(Deque<Element> elems);
	
}
