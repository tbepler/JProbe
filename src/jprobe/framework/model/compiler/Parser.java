package jprobe.framework.model.compiler;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Parser {
	
	private final Grammar m_Grammar;
	
	public Parser(Grammar g){
		m_Grammar = g;
	}
	
	public void parse(List<Token> tokens){
		Deque<Element> stack = new LinkedList<Element>();
		int index = 0;
		stack.push(new Element(tokens.get(index++)));
		while(true){
			if(m_Grammar.matches(stack)){
				m_Grammar.reduce(stack);
			}else{
				if(index >= tokens.size()){
					break;
				}
				stack.push(tokens.get(index++));
			}
		}
		if(stack.size() == 1 && m_Grammar.isStart(stack.peek())){
			//parse was succesfull
		}
		throw new ParsingException(generateBadParseMessage(tokens, stack));
	}
	
	private static String generateBadParseMessage(List<Token> tokens, Deque<Element> stack){
		String msg = "Bad parse. Input tokens: "
			+ iterableToString(tokens)
			+ " resulted in invalid parse stack: "
			+ iterableToString(stack);
		return msg;
	}
	
	private static <T> String iterableToString(Iterable<T> tokens){
		String s = "[";
		boolean first = true;
		for(T t : tokens){
			if(first){
				s += t;
				first = false;
			}else{
				s += ", " + t;
			}
		}
		s += "]";
		return s;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
