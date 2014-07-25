package language.implementation.symbols.expressions;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Production;
import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.terminals.Identifier;
import language.implementation.symbols.terminals.LParen;
import language.implementation.symbols.terminals.RParen;

public class ParenthId implements Production<Visitor>{

	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return Identifier.class;
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return ListUtil.<Class<? extends Token<Visitor>>>asUnmodifiableList(LParen.class, Identifier.class, RParen.class);
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == 3);
		assert(symbols.get(0) instanceof LParen);
		assert(symbols.get(1) instanceof Identifier);
		assert(symbols.get(2) instanceof RParen);
		return symbols.get(1);
	}

	@Override
	public int getPriority() {
		return Constants.UNPACK_PAREN_PRIORITY;
	}

	@Override
	public Assoc getAssoc() {
		return Constants.UNPACK_PAREN_ASSOC;
	}

}
