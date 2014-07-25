package language.implementation.symbols.types;

import language.implementation.Visitor;
import language.implementation.symbols.terminals.LParen;
import language.implementation.symbols.terminals.RParen;

public class TupleType extends Type{
	private static final long serialVersionUID = 1L;
	
	public final TypeList ts;
	
	public TupleType(LParen l, TypeList types, RParen r){
		this.ts = types;
	}

	@Override
	public void accept(Visitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ts == null) ? 0 : ts.hashCode());
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
		TupleType other = (TupleType) obj;
		if (ts == null) {
			if (other.ts != null)
				return false;
		} else if (!ts.equals(other.ts))
			return false;
		return true;
	}
	
	

}
