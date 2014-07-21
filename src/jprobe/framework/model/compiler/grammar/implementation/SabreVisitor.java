package jprobe.framework.model.compiler.grammar.implementation;

import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.BooleanExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.CharExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.DoubleExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.IdentifierExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.IntExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.StringExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.BooleanLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.CharLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.DoubleLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.Identifier;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.IntLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.StringLiteral;

public interface SabreVisitor {
	
	public void visit(IdentifierExp e);
	public void visit(StringExp e);
	public void visit(CharExp e);
	public void visit(IntExp e);
	public void visit(DoubleExp e);
	public void visit(BooleanExp e);
	
	public void visit(Identifier id);
	public void visit(StringLiteral s);
	public void visit(CharLiteral c);
	public void visit(IntLiteral n);
	public void visit(DoubleLiteral n);
	public void visit(BooleanLiteral b);
	
}
