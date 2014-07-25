package language.implementation.symbols.expressions;

import java.util.Collection;

import language.compiler.grammar.Production;
import language.implementation.Visitor;

public class ExpressionRules {
	
	public static void addAll(Collection<Production<Visitor>> col){
		col.add(new BlockExp());
		col.add(new FunctionExp(null, null));
		col.add(new IdentifierExp(null));
		col.add(new ParenthId());
	}
	
}
