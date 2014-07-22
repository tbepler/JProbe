package jprobe.framework.model.compiler.grammar.implementation;

import java.util.ArrayList;
import java.util.List;

import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.SymbolsFactory;
import jprobe.framework.model.compiler.grammar.implementation.symbols.EOF;
import jprobe.framework.model.compiler.grammar.implementation.symbols.ErrorToken;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Program;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Terminal;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Whitespace;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.BooleanExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.CharExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.DoubleExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.IdentifierExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.IntExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.expressions.StringExp;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.BooleanLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.CharLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.Comma;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.DoubleLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.Identifier;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.IntLiteral;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.LParen;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.RParen;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.Semicolon;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.StringLiteral;

public class SabreSymbolsFactory implements SymbolsFactory<SabreVisitor>{	
	
	

	@Override
	public Symbol<SabreVisitor> newEOFSymbol() {
		return new EOF();
	}
	
	@Override
	public Production<SabreVisitor> newStartProduction() {
		return new Program(null);
	}

	@Override
	public List<Production<SabreVisitor>> newProductions() {
		List<Production<SabreVisitor>> list = new ArrayList<Production<SabreVisitor>>();
		//list.add(new Program(null));
		list.add(new BooleanExp(null));
		list.add(new CharExp(null));
		list.add(new DoubleExp(null));
		list.add(new IdentifierExp(null));
		list.add(new IntExp(null));
		list.add(new StringExp(null));
		return list;
	}

	@Override
	public List<Terminal> newTerminals() {
		List<Terminal> list = new ArrayList<Terminal>();
		list.add(new BooleanLiteral(false));
		list.add(new CharLiteral(' '));
		list.add(new Comma());
		list.add(new DoubleLiteral(1));
		list.add(new IntLiteral(1));
		list.add(new LParen());
		list.add(new RParen());
		list.add(new Semicolon());
		list.add(new StringLiteral(""));
		list.add(new Identifier(""));
		list.add(new Whitespace());
		list.add(new ErrorToken(""));
		return list;
		
	}

}
