package language.implementation.symbols;

import java.lang.reflect.Constructor;
import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Token;
import language.implementation.Visitor;

public abstract class Declaration extends Rule<Visitor>{
	private static final long serialVersionUID = 1L;

	private final Constructor<? extends Declaration> cons;
	protected final List<Class<? extends Token<Visitor>>> rhs;
	
	protected Declaration(Class<? extends Declaration> clazz, Class<? extends Token<Visitor>> ... rhs){
		try {
			this.cons = clazz.getConstructor(rhs);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		this.rhs = ListUtil.asUnmodifiableList(rhs);
	}
	
	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return Declaration.class;
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return this.rhs;
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		try {
			return cons.newInstance(symbols.toArray());
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
		return Declaration.class;
	}
}

