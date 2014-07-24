package language.implementation;

import language.implementation.symbols.ErrorToken;
import language.implementation.symbols.Program;
import language.implementation.symbols.expressions.BooleanExp;
import language.implementation.symbols.expressions.CharExp;
import language.implementation.symbols.expressions.DivExp;
import language.implementation.symbols.expressions.DoubleExp;
import language.implementation.symbols.expressions.ExponentExp;
import language.implementation.symbols.expressions.IdentifierExp;
import language.implementation.symbols.expressions.IntExp;
import language.implementation.symbols.expressions.MinusExp;
import language.implementation.symbols.expressions.MultExp;
import language.implementation.symbols.expressions.PlusExp;
import language.implementation.symbols.expressions.StringExp;
import language.implementation.symbols.lists.StmList;
import language.implementation.symbols.statements.AssignStm;
import language.implementation.symbols.terminals.BooleanLiteral;
import language.implementation.symbols.terminals.CharLiteral;
import language.implementation.symbols.terminals.DoubleLiteral;
import language.implementation.symbols.terminals.Identifier;
import language.implementation.symbols.terminals.IntLiteral;
import language.implementation.symbols.terminals.StringLiteral;

public interface Visitor {
	
	public void visit(Program p);
	
	public void visit(StmList s);
	
	public void visit(AssignStm s);
	
	public void visit(PlusExp e);
	public void visit(MinusExp e);
	public void visit(MultExp e);
	public void visit(DivExp e);
	public void visit(ExponentExp e);
	
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
	
	public void visit(ErrorToken e);
	
}
