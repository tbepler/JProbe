package language.implementation.symbols.expressions.assignment;

import language.implementation.Visitor;
import language.implementation.symbols.Expression;
import language.implementation.symbols.lists.IdList;
import language.implementation.symbols.terminals.Assign;

public class VarAssignment extends Assignment{
	private static final long serialVersionUID = 1L;
	
	public final IdList ids;
	public final Expression e;
	
	public VarAssignment(IdList id, Assign a, Expression e){ this.ids = id; this.e = e; }

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((ids == null) ? 0 : ids.hashCode());
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
		VarAssignment other = (VarAssignment) obj;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		if (ids == null) {
			if (other.ids != null)
				return false;
		} else if (!ids.equals(other.ids))
			return false;
		return true;
	}
	
	

}
