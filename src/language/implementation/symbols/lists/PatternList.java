package language.implementation.symbols.lists;

import java.util.Collections;
import java.util.List;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.patterns.Pattern;

public class PatternList extends ListStartRule<Pattern>{
	private static final long serialVersionUID = 1L;

	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return PatternList.class;
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return Collections.emptyList();
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		assert(symbols.isEmpty());
		return new PatternList();
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
		return PatternList.class;
	}

}
