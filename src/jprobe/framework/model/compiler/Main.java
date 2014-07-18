package jprobe.framework.model.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import jprobe.framework.model.compiler.lexer.Lexer;
import jprobe.framework.model.compiler.lexer.Token;
import jprobe.framework.model.compiler.lexer.TokenTypes;

public class Main {
	
	public static void main(String[] args){
		
		Lexer<TokenTypes> lexer = new Lexer<TokenTypes>(TokenTypes.values());
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		List<Token<TokenTypes>> tokens;
		try {
			while((line = reader.readLine()) != null){
				if(line.matches("[qQ]([uU][iI][tT])?")){
					break;
				}
				tokens = lexer.lex(line);
				printTokens(tokens, System.out);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				//derp
				e.printStackTrace();
			}
		}
		
		
	}
	
	private static <T> void printTokens(Iterable<Token<T>> tokens, PrintStream out){
		boolean first = true;
		for(Token<T> t : tokens){
			if(first){
				first = false;
			}else{
				out.print(" ");
			}
			out.print(t);
		}
		out.println();
	}
	
}
