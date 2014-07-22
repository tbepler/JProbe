package jprobe.framework.model.compiler.grammar.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jprobe.framework.model.compiler.grammar.Grammar;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.lexer.Lexer;
import jprobe.framework.model.compiler.parser.Parser;
import jprobe.framework.model.compiler.parser.ParserBuilder;

public class Main {
	
	public static void main(String[] args){
		
		Grammar<Visitor> grammar = new SabreGrammar();
		ParserBuilder<Visitor> builder = new ParserBuilder<Visitor>(grammar);
		Parser<Visitor> parser = builder.newParser();
		
		Visitor v = new PrintVisitor();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		//List<Symbol<SabreVisitor>> tokens;
		Lexer<Visitor> lexer;
		try {
			while((line = reader.readLine()) != null){
				if(line.matches("[qQ]([uU][iI][tT])?")){
					break;
				}
				lexer = new Lexer<Visitor>(grammar, line);
				while(lexer.hasNext()){
					System.out.print(lexer.nextToken()+" ");
				}
				System.out.println();
				lexer = new Lexer<Visitor>(grammar, line);
				try{
					Symbol<Visitor> term = parser.parse(lexer);
					term.accept(v);
				} catch(RuntimeException e){
					System.err.println(e.getMessage());
				}
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
