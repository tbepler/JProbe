package language.implementation.symbols.expressions.assignment;

import language.implementation.Visitor;
import language.implementation.symbols.expressions.Expression;
import language.implementation.symbols.lists.IdList;
import language.implementation.symbols.lists.PatternList;
import language.implementation.symbols.terminals.Assign;
import language.implementation.symbols.terminals.FunKeyword;

public class FuncAssignment extends Assignment{
	private static final long serialVersionUID = 1L;
	
	public final IdList ids;
	public final PatternList pats;
	public final Expression e;
	
	public FuncAssignment(FunKeyword keyword, IdList id, PatternList pats, Assign a, Expression e){
		this.ids = id;
		this.pats = pats;
		this.e = e;
	}

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
		result = prime * result + ((pats == null) ? 0 : pats.hashCode());
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
		FuncAssignment other = (FuncAssignment) obj;
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
		if (pats == null) {
			if (other.pats != null)
				return false;
		} else if (!pats.equals(other.pats))
			return false;
		return true;
	}
	
	

}
