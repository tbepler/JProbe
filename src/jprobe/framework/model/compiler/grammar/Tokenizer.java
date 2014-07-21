package jprobe.framework.model.compiler.grammar;

public interface Tokenizer<V> {
	
	public String getRegex();
	
	public Symbol<V> tokenize(String s);
	
}
