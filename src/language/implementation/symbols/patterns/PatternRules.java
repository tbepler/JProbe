package language.implementation.symbols.patterns;

import java.util.Collection;

import language.compiler.grammar.Production;
import language.implementation.Visitor;

public class PatternRules {
	
	public static void addAll(Collection<Production<Visitor>> col){
		col.add(new IdPattern(null));
	}

}
