package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This data structure maps keys to values using a prefix tree. This is achieved by indexing the value
 * by the string returned by key.toString(). Key objects must have the property that given two keys,
 * a and b, a.toString().equals(b.toString()) iff a.equals(b). If this condition is not met, then
 * the behavior of this data structure is undefined.
 * 
 * @author Tristan Bepler
 *
 * @param <K> - the class used as keys
 * @param <V> - the class used as values
 */
public class Dictionary<K,V> implements Serializable, Iterable<K>{
	private static final long serialVersionUID = 1L;
	
	private class Node implements Serializable{
		private static final long serialVersionUID = 1L;
		
		public V value;
		public char edge;
		public Collection<Node> children;
		
		public Node(V value, char edge){
			this.value = value;
			this.edge = edge;
			children = new ArrayList<Node>();
		}
	}
	
	private final Set<K> m_KeySet = new HashSet<K>();
	private final Node m_Root = new Node(null, '~');
	private final Character m_Wildcard;
	
	/**
	 * Creates an empty dictionary with no wildcard character.
	 */
	public Dictionary(){
		m_Wildcard = null;
	}
	
	/**
	 * Returns the number of entries in this dictionary.
	 * @return
	 */
	public int size(){
		return m_KeySet.size();
	}
	
	/**
	 * Creates an empty dictionary that uses the given wildcard character.
	 * @param wildcard
	 */
	public Dictionary(char wildcard){
		m_Wildcard = wildcard;
	}
	
	/**
	 * Returns the wildcard character specified for this dictionary or null if
	 * none was specified.
	 * @return
	 */
	public Character getWildcard(){
		return m_Wildcard;
	}
	
	/**
	 * Returns true if there exists at least one non-null value for the given key.
	 * @param key
	 * @return
	 */
	public boolean contains(K key){
		List<V> values = this.get(key);
		return !values.isEmpty();
	}
	
	/**
	 * Removes the mapping for the given key from this dictionary, if it exists.
	 * @param key - key to remove
	 */
	public void remove(K key){
		if(m_KeySet.contains(key)){
			this.put(key, null);
		}
	}
	
	/**
	 * Searches this dictionary for the given key. Returns a list of all non-null values matching the key.
	 * This can only be more than one value if a wildcard character was specified.
	 * @param key - key to lookup in this dictionary
	 * @return a list of non-null values
	 */
	public List<V> get(K key){
		String s = key == null ? "" : key.toString();
		//initialize current node set to contain the root node
		Set<Node> cur = new HashSet<Node>();
		cur.add(m_Root);
		//iterate over each character in the string key
		for(char c : s.toCharArray()){
			//if the cur node set is empty stop searching
			if(cur.isEmpty()) break;
			//create next node set
			Set<Node> next = new HashSet<Node>();
			//iterate over current nodes and put their children that match c into the next set
			for(Node n : cur){
				for(Node child : n.children){
					if(new Character(child.edge).equals(m_Wildcard) || c == child.edge){
						next.add(child);
					}
				}
			}
			//update the cur set to the next set
			cur = next;
		}
		//create the results list
		List<V> results = new ArrayList<V>();
		//iterate over the final nodes and put their non-null values into the results list
		for(Node leaf : cur){
			if(leaf.value != null){
				results.add(leaf.value);
			}
		}
		//return results
		return results;
	}
	
	/**
	 * Puts the given key, value pair into this dictionary. Keys are mapped by using their toString() method. Therefore,
	 * given two keys a and b a.toString().equals(b.toString()) should be true iff a.equals(b). If this condition is
	 * not met, then the behavior of this data structure is not guaranteed. Keys of null are mapped to the empty
	 * string. Furthermore, values of null are not mapped. put(key, null) is equivalent to remove(key).
	 * @param key - key that the given value should be indexed by
	 * @param value - value to associate with the given key
	 */
	public void put(K key, V value){
		if(value == null){
			if(m_KeySet.contains(key)){
				m_KeySet.remove(key);
			}
			return;
		}
		//put key in keySet
		m_KeySet.add(key);
		String s = key == null ? "" : key.toString();
		//initialize cur node to root node
		Node cur = m_Root;
		//iterate over each char of the string
		for(char c : s.toCharArray()){
			//initialize next node to null
			Node next = null;
			//iterate over children of cur and set next to child if edge matches c
			for(Node child : cur.children){
				if(c == child.edge){
					next = child;
				}
			}
			//if no child edge matched c, create new child for c
			if(next == null){
				next = new Node(null, c);
				cur.children.add(next);
			}
			//set cur to next
			cur = next;
		}
		//cur node is now the node corresponding to final char of s, set val of cur to given value
		cur.value = value;
	}
	
	/**
	 * Returns an unmodifiable view of the keys for this dictionary.
	 * @return
	 */
	public Set<K> keySet(){
		return Collections.unmodifiableSet(m_KeySet);
	}
	
	/**
	 * Returns a collection of all non-null values in this dictionary.
	 * @return
	 */
	public Collection<V> values(){
		List<V> values = new ArrayList<V>();
		for(K key : this){
			values.addAll(this.get(key));
		}
		return Collections.unmodifiableList(values);
	}
	
	@Override
	public Iterator<K> iterator() {
		return m_KeySet.iterator();
	}
	
}
