package jprobe.framework.model.compiler.grammar.implementation.symbols.terminators;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;

public class CharLiteral extends Symbol<SabreVisitor> {
	private static final long serialVersionUID = 1L;
	
	public String s;
	
	public CharLiteral(String s){ this.s = s; }

	@Override
	public void accept(SabreVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Symbol<SabreVisitor>> getSymbolType() {
		return CharLiteral.class;
	}
	
}
