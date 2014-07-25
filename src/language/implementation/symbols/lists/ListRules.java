package language.implementation.symbols.lists;

import java.util.Collection;

import language.compiler.grammar.Production;
import language.implementation.Visitor;

public class ListRules {
	
	public static void addAll(Collection<Production<Visitor>> col){
		col.add(new IdList());
		col.add(new IdListAppend());
		col.add(new PatternList());
		col.add(new PatternListAppend());
	}
	
}
