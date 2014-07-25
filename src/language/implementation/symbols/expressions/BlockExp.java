package language.implementation.symbols.expressions;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Production;
import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.terminals.LParen;
import language.implementation.symbols.terminals.RParen;

public class BlockExp implements Production<Visitor>{

	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return Expression.class;
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return ListUtil.asUnmodifiableList(LParen.class, Expression.class, RParen.class);
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == 3);
		assert(symbols.get(0) instanceof LParen);
		assert(symbols.get(1) instanceof Expression);
		assert(symbols.get(2) instanceof RParen);
		return symbols.get(1);
	}

	@Override
	public int getPriority() {
		return Constants.DEFAULT_PRIORITY;
	}

	@Override
	public Assoc getAssoc() {
		return Constants.DEFAULT_ASSOC;
	}
	

}
