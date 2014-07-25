package language.implementation.symbols.types;

import language.implementation.symbols.lists.ListAppendRule;
import language.implementation.symbols.terminals.Comma;

public class TypeListAppend extends ListAppendRule<Type>{
	private static final long serialVersionUID = 1L;

	public TypeListAppend() {
		super(TypeList.class, Comma.class, Type.class);
	}

}
