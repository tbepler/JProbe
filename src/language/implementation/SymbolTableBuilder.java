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

public class SymbolTableBuilder implements Visitor{

	@Override
	public void visit(Program p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StmList s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AssignStm s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PlusExp e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MinusExp e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MultExp e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DivExp e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExponentExp e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IdentifierExp e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StringExp e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CharExp e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IntExp e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DoubleExp e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BooleanExp e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Identifier id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StringLiteral s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CharLiteral c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IntLiteral n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DoubleLiteral n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BooleanLiteral b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ErrorToken e) {
		// TODO Auto-generated method stub
		
	}

}
