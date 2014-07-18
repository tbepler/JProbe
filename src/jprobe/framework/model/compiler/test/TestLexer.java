package jprobe.framework.model.compiler.test;

import java.util.List;

import jprobe.framework.model.compiler.Lexer;
import jprobe.framework.model.compiler.Token;
import jprobe.framework.model.compiler.Tokenizer;

public class TestLexer extends junit.framework.TestCase{
	
	private enum Type{
		ID,
		NUM,
		STR;
	}
	
	private static class Identifier extends Token<Type>{

		public Identifier(String s) {
			super(s,Type.ID);
		}
		
	}
	
	private static class NumericLiteral extends Token<Type>{

		public NumericLiteral(String s) {
			super(s,Type.NUM);
		}
		
	}
	
	private static class StringLiteral extends Token<Type>{

		public StringLiteral(String s) {
			super(s,Type.STR);
		}
		
	}
	
	private final String identifierRegex = "[a-zA-Z_]+[a-zA-Z0-9_]*";
	private final Tokenizer<Type> identifierTokenizer = new Tokenizer<Type>(identifierRegex){

		@Override
		public Token<Type> tokenize(String s) {
			return new Identifier(s);
		}
		
	};
	
	private final String numericRegex = "-?[0-9]+(\\.[0-9]+)?";
	private final Tokenizer<Type> numericTokenizer = new Tokenizer<Type>(numericRegex){

		@Override
		public Token<Type> tokenize(String s) {
			return new NumericLiteral(s);
		}
		
	};
	
	//private final String stringRegex = "([\"\'])(?:(?=(\\\\?))\\2.)*?\\1";
	private final String stringRegex = "\"(?:\\\\\"|[^\"])*?\"";
	private final Tokenizer<Type> stringTokenizer = new Tokenizer<Type>(stringRegex){

		@Override
		public Token<Type> tokenize(String s) {
			return new StringLiteral(s);
		}
		
	};
	
	@SuppressWarnings("unchecked")
	private final Lexer<Type> lexer = new Lexer<Type>(stringTokenizer, numericTokenizer, identifierTokenizer);
	
	public void testLexer(){
		String s = "_identifier 0.0001 \" a string\\\" 56 literal\"  -5 ";
		List<Token<Type>> tokens = lexer.lex(s);
		
		for(Token<Type> t : tokens){
			System.out.println(t.text());
		}
		
		assertEquals(4, tokens.size());
		
		Token<Type> token0 = tokens.get(0);
		assertTrue(Identifier.class.isInstance(token0));
		assertEquals("_identifier", token0.text());
		
		Token<Type> token1 = tokens.get(1);
		assertTrue(NumericLiteral.class.isInstance(token1));
		assertEquals("0.0001", token1.text());
		
		Token<Type> token2 = tokens.get(2);
		assertTrue(StringLiteral.class.isInstance(token2));
		assertEquals("\" a string\\\" 56 literal\"", token2.text());
		
		Token<Type> token3 = tokens.get(3);
		assertTrue(NumericLiteral.class.isInstance(token3));
		assertEquals("-5", token3.text());
		
	}
	
	
	
}
