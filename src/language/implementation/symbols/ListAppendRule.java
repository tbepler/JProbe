package language.implementation.symbols;

import java.io.Serializable;
import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Production;
import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.terminals.Comma;

public class ListAppendRule<E extends Token<Visitor>> implements Production<Visitor>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private final List<Class<? extends Token<Visitor>>> rhs;
	private final Class<? extends ListStartRule<E>> listClass;
	private final Class<E> elemClass;
	
	protected ListAppendRule(Class<? extends ListStartRule<E>> listClass, Class<E> elemClass){
		this.listClass = listClass; this.elemClass = elemClass;
		rhs = ListUtil.asUnmodifiableList(listClass, Comma.class, elemClass);
	}
	
	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return listClass;
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return rhs;
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == rhs.size());
		for( int i = 0 ; i < rhs.size() ; ++i ){
			assert(rhs.get(i).isAssignableFrom(symbols.get(i).getSymbolType()));
		}
		ListStartRule<E> list = listClass.cast(symbols.get(0));
		E elem = elemClass.cast(symbols.get(2));
		list.append(elem);
		return list;
	}

	@Override
	public int getPriority() {
		return Constants.LIST_APPEND_PRIORITY;
	}

	@Override
	public Assoc getAssoc() {
		return Constants.LIST_APPEND_ASSOC;
	}

}
