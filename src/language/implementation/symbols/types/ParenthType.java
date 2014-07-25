package language.implementation.symbols.types;

import language.implementation.Visitor;
import language.implementation.symbols.terminals.LParen;
import language.implementation.symbols.terminals.RParen;

public class ParenthType extends Type{
	private static final long serialVersionUID = 1L;
	
	public final Type t;
	
	public ParenthType(LParen l, Type t, RParen r){ this.t = t; }

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString(){
		return "( " + t + " )";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((t == null) ? 0 : t.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParenthType other = (ParenthType) obj;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		return true;
	}
	
	

}
