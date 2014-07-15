package jprobe.system.controller;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jprobe.framework.model.function.FunctionFactory;

public class ParserImpl {
	
	private static final String SEP = "\\s";
	private static final char LITERAL_OPEN = '"';
	private static final char LITERAL_CLOSE = '"';
	private static final char TUPLE_OPEN = '(';
	private static final char TUPLE_CLOSE = ')';
	private static final char ESCAPE_STR = '\\';
	
	private static final int LITERAL = 0;
	private static final int TUPLE = 1;
	private static final int VARIABLE = 2;
	
	private static class Token{
		public final String str;
		public final int type;
		public Token(String str, int type){
			this.str = str; this.type = type;
		}
	}
	
	private final FunctionFactory m_Factory;
	private final Map<String, Object> m_Vars = new HashMap<String, Object>();
	private final List<Tokenizer> m_Tokenizers = new ArrayList<Tokenizer>();
	
	private Object nextToken(Reader r){
		Deque<Object> tokens = new LinkedList<Object>();
		int in;
		char c;
		try {
			while((in = r.read()) != -1){
				c = (char) in;
				if(!Character.isSpaceChar(c)){
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Object[] tokenize(String s){
		Deque<Object> deck = new LinkedList<Object>();
		
	}
	
}
