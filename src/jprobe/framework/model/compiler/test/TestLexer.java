package jprobe.framework.model.compiler.test;

import java.util.List;

import jprobe.framework.model.compiler.lexer.Lexer;
import jprobe.framework.model.compiler.lexer.Token;
import jprobe.framework.model.compiler.lexer.TokenTypes;

public class TestLexer extends junit.framework.TestCase{
	
	private final Lexer<TokenTypes> lexer = new Lexer<TokenTypes>(TokenTypes.values());
	
	public void testLexer(){
		String s = "_identifier 0.0001 \" a string\\\" 56 literal\"  -5  (a,b) ; ";
		List<Token<TokenTypes>> tokens = lexer.lex(s);
		
		for(Token<TokenTypes> t : tokens){
			System.out.println(t.text());
		}
		
		assertEquals(10, tokens.size());
		
		Token<TokenTypes> token0 = tokens.get(0);
		assertEquals(TokenTypes.ID, token0.id());
		assertEquals("_identifier", token0.text());
		
		Token<TokenTypes> token1 = tokens.get(1);
		assertEquals(TokenTypes.NUM, token1.id());
		assertEquals("0.0001", token1.text());
		
		Token<TokenTypes> token2 = tokens.get(2);
		assertEquals(TokenTypes.STR, token2.id());
		assertEquals("\" a string\\\" 56 literal\"", token2.text());
		
		Token<TokenTypes> token3 = tokens.get(3);
		assertEquals(TokenTypes.NUM, token3.id());
		assertEquals("-5", token3.text());
		
		Token<TokenTypes> token = tokens.get(4);
		assertEquals(TokenTypes.L_PAREN, token.id());
		assertEquals("(", token.text());
		
		token = tokens.get(5);
		assertEquals(TokenTypes.ID, token.id());
		assertEquals("a", token.text());
		
		token = tokens.get(6);
		assertEquals(TokenTypes.COMMA, token.id());
		assertEquals(",", token.text());
		
		token = tokens.get(7);
		assertEquals(TokenTypes.ID, token.id());
		assertEquals("b", token.text());
		
		token = tokens.get(8);
		assertEquals(TokenTypes.R_PAREN, token.id());
		assertEquals(")", token.text());
		
		token = tokens.get(9);
		assertEquals(TokenTypes.SEMICOLON, token.id());
		assertEquals(";", token.text());
		
	}
	
	
	
}
