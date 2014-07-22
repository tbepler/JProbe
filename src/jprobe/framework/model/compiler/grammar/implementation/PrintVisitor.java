package jprobe.framework.model.compiler.grammar.implementation;

import java.io.PrintStream;

import jprobe.framework.model.compiler.grammar.implementation.symbols.ErrorToken;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Program;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.BooleanExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.CharExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.DoubleExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.IdentifierExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.IntExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.StringExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.lists.StmList;
import jprobe.framework.model.compiler.grammar.implementation.symbols.statements.AssignStm;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.BooleanLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.CharLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.DoubleLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.Identifier;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.IntLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.StringLiteral;

public class PrintVisitor implements Visitor{
	
	private static final String DELIM = "  ";
	private static final PrintStream OUT = System.out;
	
	private int indent = 0;
	
	private void println(Object o){
		for( int i = 0 ; i < indent ; ++i ){
			OUT.print(DELIM);
		}
		OUT.println(o);
	}
	
	@Override
	public void visit(Program p) {
		this.println(p);
		this.println("{");
		++indent;
		p.stm.accept(this);
		--indent;
		this.println("}");
	}
	
	@Override
	public void visit(StmList s){
		this.println(s);
		this.println("{");
		++indent;
		s.left.accept(this);
		s.right.accept(this);
		--indent;
		this.println("}");
	}

	@Override
	public void visit(AssignStm s) {
		this.println(s);
		this.println("{");
		++indent;
		s.left.accept(this);
		s.right.accept(this);
		--indent;
		this.println("}");
	}

	@Override
	public void visit(IdentifierExp e) {
		this.println(e);
		this.println("{");
		++indent;
		e.id.accept(this);
		--indent;
		this.println("}");
	}

	@Override
	public void visit(StringExp e) {
		this.println(e);
		this.println("{");
		++indent;
		e.s.accept(this);
		--indent;
		this.println("}");
	}

	@Override
	public void visit(CharExp e) {
		this.println(e);
		this.println("{");
		++indent;
		e.c.accept(this);
		--indent;
		this.println("}");
	}

	@Override
	public void visit(IntExp e) {
		this.println(e);
		this.println("{");
		++indent;
		e.n.accept(this);
		--indent;
		this.println("}");
	}

	@Override
	public void visit(DoubleExp e) {
		this.println(e);
		this.println("{");
		++indent;
		e.n.accept(this);
		--indent;
		this.println("}");
	}

	@Override
	public void visit(BooleanExp e) {
		this.println(e);
		this.println("{");
		++indent;
		e.b.accept(this);
		--indent;
		this.println("}");
	}

	@Override
	public void visit(Identifier id) {
		this.println(id);
	}

	@Override
	public void visit(StringLiteral s) {
		this.println(s);
	}

	@Override
	public void visit(CharLiteral c) {
		this.println(c);
	}

	@Override
	public void visit(IntLiteral n) {
		this.println(n);
	}

	@Override
	public void visit(DoubleLiteral n) {
		this.println(n);
	}

	@Override
	public void visit(BooleanLiteral b) {
		this.println(b);
	}

	@Override
	public void visit(ErrorToken e) {
		this.println(e);
	}

}
