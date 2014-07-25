package language.implementation.symbols.lists;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.terminals.Comma;
import language.implementation.symbols.terminals.Identifier;

public class IdentifierListAppend extends IdentifierList{
	private static final long serialVersionUID = 1L;
	
	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide(){
		return ListUtil.asList(IdentifierList.class, Comma.class, Identifier.class);
	}
	
	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> tokens){
		assert(tokens.size() == 3);
		assert(tokens.get(0) instanceof IdentifierList);
		assert(tokens.get(1) instanceof Comma);
		assert(tokens.get(2) instanceof Identifier);
		IdentifierList list = (IdentifierList) tokens.get(0);
		list.append((Identifier) tokens.get(2));
		return list;
	}
	
	@Override
	public int getPriority(){
		return Constants.LIST_APPEND_PRIORITY;
	}
	
	@Override
	public Assoc getAssoc(){
		return Constants.LIST_APPEND_ASSOC;
	}

}