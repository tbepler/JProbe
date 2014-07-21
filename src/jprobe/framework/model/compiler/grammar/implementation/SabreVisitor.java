package jprobe.framework.model.compiler.grammar.implementation;

import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.IdentifierExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminators.CharLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminators.Identifier;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminators.NumericLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminators.StringLiteral;

public interface SabreVisitor {
	
	public void visit(IdentifierExp e);
	
	public void visit(Identifier id);
	public void visit(StringLiteral s);
	public void visit(CharLiteral c);
	public void visit(NumericLiteral n);
	
}
