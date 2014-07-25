package language.implementation.symbols.lists;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.ListStartRule;
import language.implementation.symbols.terminals.Identifier;

public class IdList extends ListStartRule<Identifier>{
	private static final long serialVersionUID = 1L;

	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return IdList.class;
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return ListUtil.<Class<? extends Token<Visitor>>>asUnmodifiableList(Identifier.class);
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == 1);
		assert(symbols.get(0) instanceof Identifier);
		IdList list = new IdList();
		list.append((Identifier) symbols.get(0));
		return list;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
		return IdList.class;
	}



}
