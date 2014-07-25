package language.implementation.symbols.expressions.assignment;

import language.implementation.Visitor;
import language.implementation.symbols.Expression;
import language.implementation.symbols.lists.PatternList;
import language.implementation.symbols.terminals.Assign;
import language.implementation.symbols.terminals.FunKeyword;
import language.implementation.symbols.Declaration;

public class DeclFuncAssignment extends Assignment{
	private static final long serialVersionUID = 1L;
	
	public final Declaration d;
	public final PatternList pats;
	public final Expression e;
	
	public DeclFuncAssignment(FunKeyword keyword, Declaration d, PatternList pats, Assign a, Expression e){
		this.d = d;
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
		result = prime * result + ((d == null) ? 0 : d.hashCode());
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
		DeclFuncAssignment other = (DeclFuncAssignment) obj;
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
		if (pats == null) {
			if (other.pats != null)
				return false;
		} else if (!pats.equals(other.pats))
			return false;
		return true;
	}
	
	

}
