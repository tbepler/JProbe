package language.implementation.symbols.types;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.lists.ListStartRule;
import language.implementation.symbols.terminals.Comma;

public class TypeList extends ListStartRule<Type>{
	private static final long serialVersionUID = 1L;

	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return TypeList.class;
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return ListUtil.<Class<? extends Token<Visitor>>>asUnmodifiableList(Type.class, Comma.class, Type.class);
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == 3);
		assert(symbols.get(0) instanceof Type);
		assert(symbols.get(1) instanceof Comma);
		assert(symbols.get(2) instanceof Type);
		TypeList list = new TypeList();
		list.append((Type) symbols.get(0));
		list.append((Type) symbols.get(2));
		return list;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
		return TypeList.class;
	}

}
