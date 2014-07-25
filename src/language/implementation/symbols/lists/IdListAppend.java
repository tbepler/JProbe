package language.implementation.symbols.lists;

import language.implementation.symbols.terminals.Comma;
import language.implementation.symbols.terminals.Identifier;

public class IdListAppend extends ListAppendRule<Identifier>{
	private static final long serialVersionUID = 1L;

	protected IdListAppend() {
		super(IdList.class, Comma.class, Identifier.class);
	}

}
