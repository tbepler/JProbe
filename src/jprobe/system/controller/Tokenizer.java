package jprobe.system.controller;

public interface Tokenizer {
	
	public char startChar();
	
	public char endChar();
	
	public Object tokenize(String s);
	
}
