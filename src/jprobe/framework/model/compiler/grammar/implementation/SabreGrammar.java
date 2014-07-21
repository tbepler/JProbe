package jprobe.framework.model.compiler.grammar.implementation;

import java.util.Collection;
import java.util.Iterator;

import jprobe.framework.model.compiler.grammar.Grammar;
import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;

public class SabreGrammar implements Grammar<SabreVisitor>{
	
	public SabreGrammar(){
		
	}

	@Override
	public Iterator<Production<SabreVisitor>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<SabreVisitor> getTerminalSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTerminal(Symbol<SabreVisitor> symbol) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Production<SabreVisitor>> getProductions(
			Symbol<SabreVisitor> leftHandSide) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Production<SabreVisitor>> getAllProductions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Production<SabreVisitor> getStartProduction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEOFSymbol(Symbol<SabreVisitor> symbol) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Symbol<SabreVisitor> getEOFSymbol() {
		// TODO Auto-generated method stub
		return null;
	}

}
