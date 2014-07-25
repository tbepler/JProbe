package language.implementation.symbols.declarations;

import language.compiler.grammar.Assoc;
import language.implementation.Visitor;
import language.implementation.symbols.Declaration;
import language.implementation.symbols.terminals.Identifier;

public class ParamDecl extends Declaration{
	private static final long serialVersionUID = 1L;
	
	public final Declaration left;
	public final Identifier right;

	@SuppressWarnings("unchecked")
	public ParamDecl(Declaration left, Identifier right) {
		super(ParamDecl.class, Declaration.class, Identifier.class);
		this.left = left; this.right = right;
	}
	
	@Override
	public Assoc getAssoc(){
		return Assoc.LEFT;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
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
		ParamDecl other = (ParamDecl) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}
	
	

}
