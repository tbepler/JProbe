package language.implementation.symbols.declarations;

import java.util.Collection;

import language.compiler.grammar.Production;
import language.implementation.Visitor;

public class DeclarationRules {
	
	public static void addAll(Collection<Production<Visitor>> col){
		col.add(new TypeDecl(null, null, null));
	}
	
}
