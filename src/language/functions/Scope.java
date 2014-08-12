package language.functions;

import java.util.Map;

import language.symboltable.PersistentTreeMap;

public class Scope{
	
	private final PersistentTreeMap<String, Thunk> map;
	
	public Scope(){
		map = new PersistentTreeMap<String, Thunk>();
	}
	
	public Scope(PersistentTreeMap<String, Thunk> map){
		this.map = map;
	}
	
	public Scope insert(String key, Thunk value){
		return new Scope(map.insert(key, value));
	}
	
	public Scope insertAll(Map<String, Thunk> map){
		return new Scope(this.map.insertAll(map));
	}
	
	public Thunk lookup(String key){
		return map.lookup(key);
	}

}
