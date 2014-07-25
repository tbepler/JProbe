package language.implementation.symbols.lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Production;
import language.compiler.grammar.Token;
import language.implementation.Equals;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.terminals.Identifier;

public class IdentifierList extends Token<Visitor> implements Production<Visitor>, Iterable<Identifier>{
	private static final long serialVersionUID = 1L;
	
	private final List<Identifier> list = new ArrayList<Identifier>();
	
	public void append(Identifier id){
		list.add(id);
	}
	
	public int size(){
		return list.size();
	}
	
	public Identifier get(int index){
		return list.get(index);
	}
	
	@Override
	public Iterator<Identifier> iterator(){
		return list.iterator();
	}

	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return IdentifierList.class;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
		return IdentifierList.class;
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return ListUtil.<Class<? extends Token<Visitor>>>asList(Identifier.class);
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == 1);
		assert(symbols.get(0) instanceof Identifier);
		IdentifierList l = new IdentifierList();
		l.append((Identifier) symbols.get(0));
		return l;
	}

	@Override
	public int getPriority() {
		return Constants.DEFAULT_PRIORITY;
	}

	@Override
	public Assoc getAssoc() {
		return Constants.DEFAULT_ASSOC;
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}
	
	@Override
	public int hashCode(){
		return Equals.hashCode(list.toArray());
	}
	
	@Override
	public boolean equals(Object o){
		if(o == this) return true;
		if(o == null) return false;
		if(o instanceof IdentifierList){
			IdentifierList that = (IdentifierList) o;
			return Equals.listEquals(this.list, that.list);
		}
		return false;
	}
	

}
