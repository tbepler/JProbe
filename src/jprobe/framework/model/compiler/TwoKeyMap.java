package jprobe.framework.model.compiler;

import java.util.HashMap;
import java.util.Map;

public class TwoKeyMap<K1,K2,V> extends HashMap<K1,Map<K2,V>> {
	private static final long serialVersionUID = 1L;

	@Override
	public Map<K2,V> get(Object k){
		return containsKey(k) ? super.get(k) : new HashMap<K2,V>();
	}
	
	public V get(Object k1, Object k2){
		return get(k1).get(k2);
	}
	
	public V put(K1 key1, K2 key2, V value){
		Map<K2,V> map = this.get(key1);
		map.put(key2, value);
		this.put(key1, map);
		return value;
	}
	
}
