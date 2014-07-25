package language.implementation.symbols.declarations;

import language.implementation.Visitor;
import language.implementation.symbols.Declaration;
import language.implementation.symbols.Type;
import language.implementation.symbols.lists.IdList;
import language.implementation.symbols.terminals.DoubleColon;

public class TypeDecl extends Declaration{
	private static final long serialVersionUID = 1L;
	
	public final Type t;
	public final IdList ids;
	
	public TypeDecl(Type t, DoubleColon c, IdList ids){
		this.t = t; this.ids = ids;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
