package language.implementation.symbols.expressions.assignment;

import language.implementation.Visitor;
import language.implementation.symbols.Declaration;
import language.implementation.symbols.expressions.Expression;
import language.implementation.symbols.terminals.Assign;

public class DeclVarAssignment extends Assignment{
	private static final long serialVersionUID = 1L;
	
	public final Declaration d;
	public final Expression e;
	
	public DeclVarAssignment(Declaration id, Assign a, Expression e){ this.d = id; this.e = e; }

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((d == null) ? 0 : d.hashCode());
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
		DeclVarAssignment other = (DeclVarAssignment) obj;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		return true;
	}
	
	

}
