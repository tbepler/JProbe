package language.implementation.symbols;

import java.lang.reflect.Constructor;
import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Production;
import language.compiler.grammar.Token;

public abstract class Rule<V> extends Token<V> implements Production<V> {
	private static final long serialVersionUID = 1L;
	
	private final Constructor<? extends Token<V>> cons;
	protected final List<Class<? extends Token<V>>> rhs;
	
	protected Rule(Class<? extends Token<V>> clazz, Class<? extends Token<V>> ... rhs){
		try {
			this.cons = clazz == null ? null : clazz.getConstructor(rhs);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		this.rhs = ListUtil.asUnmodifiableList(rhs);
	}

	@Override
	public List<Class<? extends Token<V>>> rightHandSide() {
		return this.rhs;
	}

	@Override
	public Token<V> reduce(List<Token<V>> symbols) {
		try {
			return cons.newInstance(symbols.toArray());
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public int getPriority(){
		return Constants.DEFAULT_PRIORITY;
	}
	
	@Override
	public Assoc getAssoc(){
		return Constants.DEFAULT_ASSOC;
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}

}
