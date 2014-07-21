package jprobe.framework.model.compiler.grammar.implementation.symbols;

import java.util.ArrayList;
import java.util.List;

import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;

public abstract class Expression extends Symbol<SabreVisitor> implements Production<SabreVisitor, Expression>{
	private static final long serialVersionUID = 1L;
	
	protected static <V> List<Class<? extends Symbol<V>>> asList(Class<? extends Symbol<V>> clazz){
		List<Class<? extends Symbol<V>>> list = new ArrayList<Class<? extends Symbol<V>>>();
		list.add(clazz);
		return list;
	}

	@Override
	public Class<? extends Symbol<SabreVisitor>> getSymbolType() {
		return Expression.class;
	}

	@Override
	public Class<Expression> leftHandSide() {
		return Expression.class;
	}

}
