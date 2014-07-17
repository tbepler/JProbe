package jprobe.framework.model.compiler;

import java.util.Deque;

public interface Rule {
	
	/**
	 * Tests if the elements at the head of the given
	 * deque match this rule.
	 * @param tokens
	 * @return
	 */
	public boolean matches(Deque<Element> tokens);
	
	/**
	 * Replaces the elements matching this rule from the
	 * head of the deque with the element produced by
	 * this rule.
	 * @param tokens
	 */
	public void reduce(Deque<Element> tokens);
	
	
}
