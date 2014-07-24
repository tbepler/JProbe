package language.implementation.symbols.expressions;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Token;
import language.implementation.Equals;
import language.implementation.Visitor;
import language.implementation.symbols.Expression;
import language.implementation.symbols.terminals.Identifier;

public class IdentifierExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Token<Visitor>>> RHS =
			ListUtil.<Class<? extends Token<Visitor>>>asUnmodifiableList(Identifier.class);
	
	public Identifier id;
	
	public IdentifierExp(Identifier id){ this.id = id; }

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Expression reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == 1);
		assert(symbols.get(0) instanceof Identifier);
		return new IdentifierExp((Identifier) symbols.get(0));
	}
	
	/*
	@Override
	public String toString(){
		return this.getClass().getSimpleName() +
				"\n{\n" +
				ToString.nestedToString(id) +
				"\n}";
	}
	*/
	
	@Override
	public int hashCode(){
		return Equals.hashCode(id);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof IdentifierExp){
			IdentifierExp that = (IdentifierExp)o;
			return Equals.equals(this.id, that.id);
		}
		return false;
	}

}
