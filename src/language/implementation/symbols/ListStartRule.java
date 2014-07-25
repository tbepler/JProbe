package language.implementation.symbols;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import language.compiler.grammar.Assoc;
import language.compiler.grammar.Production;
import language.compiler.grammar.Token;
import language.implementation.Equals;
import language.implementation.Visitor;

public abstract class ListStartRule<E extends Token<Visitor>> extends Token<Visitor> implements Production<Visitor>, Iterable<E>{
	private static final long serialVersionUID = 1L;

	private final List<E> list = new ArrayList<E>();
	
	public void append(E elem){
		list.add(elem);
	}
	
	public int size(){
		return list.size();
	}
	
	public E get(int index){
		return list.get(index);
	}
	
	@Override
	public Iterator<E> iterator(){
		return Collections.unmodifiableList(list).iterator();
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
	public int hashCode(){
		return Equals.hashCode(list.toArray());
	}
	
	@Override
	public boolean equals(Object o){
		if(o == this) return true;
		if(o == null) return false;
		if(!this.getClass().equals(o.getClass())) return false;
		ListStartRule<?> that = (ListStartRule<?>) o;
		return Equals.listEquals(this.list, that.list);
	}
	
	

}
