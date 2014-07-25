package language.implementation.symbols.declarations;

import language.implementation.Visitor;
import language.implementation.symbols.Declaration;
import language.implementation.symbols.Type;
import language.implementation.symbols.lists.IdentifierList;
import language.implementation.symbols.terminals.DoubleColon;

public class TypeSignDecl extends Declaration{
	private static final long serialVersionUID = 1L;

	public final Type t;
	public final IdentifierList ids;
	
	@SuppressWarnings("unchecked")
	public TypeSignDecl(Type t, DoubleColon ignored, IdentifierList ids) {
		super(TypeSignDecl.class, Type.class, DoubleColon.class, IdentifierList.class);
		this.t = t;
		this.ids = ids;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ids == null) ? 0 : ids.hashCode());
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
		TypeSignDecl other = (TypeSignDecl) obj;
		if (ids == null) {
			if (other.ids != null)
				return false;
		} else if (!ids.equals(other.ids))
			return false;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		return true;
	}
	
	

}
