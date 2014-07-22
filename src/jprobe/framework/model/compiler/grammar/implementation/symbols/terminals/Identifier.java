package jprobe.framework.model.compiler.grammar.implementation.symbols.terminals;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Terminal;

public class Identifier extends Terminal {
	private static final long serialVersionUID = 1L;
	
	public String id;
	
	public Identifier(String id){
		this.id = id;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Symbol<Visitor>> getSymbolType() {
		return Identifier.class;
	}

	@Override
	public String getRegex() {
		return Constants.ID_REGEX;
	}

	@Override
	public Symbol<Visitor> tokenize(String s) {
		return new Identifier(s);
	}
	
	@Override
	public String toString(){
		return "ID("+id+")";
	}
	
	@Override
	public int hashCode(){
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Identifier){
			return id.equals(((Identifier)o).id);
		}
		return false;
	}

}
