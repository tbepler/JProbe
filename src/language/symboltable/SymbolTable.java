package language.symboltable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import language.exceptions.SymbolAlreadyDefinedException;
import language.exceptions.UndefinedSymbolException;

/**
 * 
 * @author Wei
 *
 * @param <V>
 */

public class SymbolTable<V> {
	private SymbolTable<V> parent;
	private Map<Symbol, Collection<V>> currentTable;
	
	public SymbolTable(){
		this.parent = null;
		this.currentTable = new HashMap<Symbol, Collection<V>>();
	}
	
	public SymbolTable(SymbolTable<V> parent){
		this.parent = parent;
		this.currentTable = new HashMap<Symbol, Collection<V>>();
	}
	
	public SymbolTable<V> insert(Symbol s, V value) throws SymbolAlreadyDefinedException{
		
		Collection<V> vals = currentTable.get(s);
		if(vals == null){
			vals = new HashSet<V>();
			currentTable.put(s, vals);
		}
		if(!vals.add(value)){
			throw new SymbolAlreadyDefinedException("The current table already contains: "+ s+", "+value);
		}
		return this;
	}
	
	public Collection<V> lookup(Symbol s) throws UndefinedSymbolException{
		Collection<V> result = new HashSet<V>();
		SymbolTable<V> currParent = this.parent;
		
		if(currentTable.containsKey(s)){
			result.addAll(currentTable.get(s));
		}
		
		while(currParent!= null){
			
			if(currParent.getCurrentTable().containsKey(s)){
				result.addAll(currParent.getCurrentTable().get(s));
			}
			
			currParent = currParent.getParent();
		}
		if(result.isEmpty()){
			throw new UndefinedSymbolException("Undefined Symbol: "+s);
		}
		
		return result;
	}
	
	public SymbolTable<V> getParent(){
		return this.parent;
	}
	
	public Map<Symbol,Collection<V>> getCurrentTable(){
		return this.currentTable;
	}
}
