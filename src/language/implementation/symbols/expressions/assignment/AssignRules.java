package language.implementation.symbols.expressions.assignment;

import java.util.Collection;

import language.compiler.grammar.Production;
import language.implementation.Visitor;

public class AssignRules {
	
	public static void addAll(Collection<Production<Visitor>> col){
		col.add(new DeclFuncAssignment(null, null, null, null, null));
		col.add(new FuncAssignment(null, null, null, null, null));
		col.add(new DeclVarAssignment(null, null, null));
		col.add(new VarAssignment(null, null, null));
	}

}
