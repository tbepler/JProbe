package jprobe.framework.model.compiler.grammar.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jprobe.framework.model.compiler.grammar.Grammar;
import jprobe.framework.model.compiler.lexer.Lexer;

public class Main {
	
	public static void main(String[] args){
		
		Grammar<SabreVisitor> grammar = new SabreGrammar();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		//List<Symbol<SabreVisitor>> tokens;
		Lexer<SabreVisitor> lexer;
		try {
			while((line = reader.readLine()) != null){
				if(line.matches("[qQ]([uU][iI][tT])?")){
					break;
				}
				lexer = new Lexer<SabreVisitor>(grammar, line);
				while(lexer.hasNext()){
					System.out.print(lexer.nextToken()+" ");
				}
				System.out.println();
				//tokens = lexer.lexAllTokens();
				//printTokens(tokens, System.out);
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
	
//	private static <T> void printTokens(Iterable<Symbol<T>> tokens, PrintStream out){
//		boolean first = true;
//		for(Symbol<T> t : tokens){
//			if(first){
//				first = false;
//			}else{
//				out.print(" ");
//			}
//			out.print(t);
//		}
//		out.println();
//	}
	
}
