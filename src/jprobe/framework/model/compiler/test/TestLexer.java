package jprobe.framework.model.compiler.test;

import java.util.Arrays;
import java.util.List;

import jprobe.framework.model.compiler.Lexer;
import jprobe.framework.model.compiler.Token;
import jprobe.framework.model.compiler.Tokenizer;

public class TestLexer extends junit.framework.TestCase{
	
	private static class Identifier extends Token{

		public Identifier(String s) {
			super(s);
		}
		
	}
	
	private static class NumericLiteral extends Token{

		public NumericLiteral(String s) {
			super(s);
		}
		
	}
	
	private static class StringLiteral extends Token{

		public StringLiteral(String s) {
			super(s);
		}
		
	}
	
	private final String identifierRegex = "[a-zA-Z_]+[a-zA-Z0-9_]*";
	private final Tokenizer identifierTokenizer = new Tokenizer(identifierRegex){

		@Override
		public Token tokenize(String s) {
			return new Identifier(s);
		}
		
	};
	
	private final String numericRegex = "-?[0-9]+(\\.[0-9]+)?";
	private final Tokenizer numericTokenizer = new Tokenizer(numericRegex){

		@Override
		public Token tokenize(String s) {
			return new NumericLiteral(s);
		}
		
	};
	
	//private final String stringRegex = "([\"\'])(?:(?=(\\\\?))\\2.)*?\\1";
	private final String stringRegex = "\"(?:\\\\\"|[^\"])*?\"";
	private final Tokenizer stringTokenizer = new Tokenizer(stringRegex){

		@Override
		public Token tokenize(String s) {
			return new StringLiteral(s);
		}
		
	};
	
	private final Lexer lexer = new Lexer(Arrays.asList(stringTokenizer, numericTokenizer, identifierTokenizer));
	
	public void testLexer(){
		String s = "_identifier 0.0001 \" a string\\\" 56 literal\"  ";
		List<Token> tokens = lexer.analyze(s);
		
		for(Token t : tokens){
			System.out.println(t.getString());
		}
		
		assertEquals(3, tokens.size());
		
		Token token0 = tokens.get(0);
		assertTrue(Identifier.class.isInstance(token0));
		assertEquals("_identifier", token0.getString());
		
		Token token1 = tokens.get(1);
		assertTrue(NumericLiteral.class.isInstance(token1));
		assertEquals("0.0001", token1.getString());
		
		Token token2 = tokens.get(2);
		assertTrue(StringLiteral.class.isInstance(token2));
		assertEquals("\" a string\\\" 56 literal\"", token2.getString());
		
	}
	
	
	
}
