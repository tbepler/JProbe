package language.symboltable;

import java.util.HashMap;
import java.util.Map;

public class PersistentTreeMap<K extends Comparable<K>,V> {
	
	private final Map<K,V> map = new HashMap<K,V>();
	
	public PersistentTreeMap(){
		//TODO
	}
	
	public PersistentTreeMap(Map<K,V> map){
		this.map.putAll(map);
	}
	
	public PersistentTreeMap<K,V> insert(K key, V value){
		Map<K,V> copy = new HashMap<K,V>(map);
		copy.put(key, value);
		return new PersistentTreeMap<K,V>(copy);
	}
	
	public PersistentTreeMap<K,V> insertAll(Map<K,V> map){
		Map<K,V> copy = new HashMap<K,V>(this.map);
		copy.putAll(map);
		return new PersistentTreeMap<K,V>(copy);
	}
	
	public V lookup(K key){
		return map.get(key);
	}
	
	@Override
	public int hashCode(){
		//TODO
		return 0;
	}
	
	@Override
	public boolean equals(Object o){
		//TODO
		return false;
	}
	
}
