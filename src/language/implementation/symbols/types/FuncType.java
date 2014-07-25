package language.implementation.symbols.types;

import language.compiler.grammar.Assoc;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Type;
import language.implementation.symbols.terminals.Arrow;

public class FuncType extends Type{
	private static final long serialVersionUID = 1L;
	
	public final Type left;
	public final Type right;
	
	public FuncType(Type left, Arrow arrow, Type right){
		this.left = left; this.right = right;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "( " + left + " -> " + right + " )";
	}
	
	@Override
	public Assoc getAssoc(){
		return Constants.FUNC_TYPE_ASSOC;
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
		FuncType other = (FuncType) obj;
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
