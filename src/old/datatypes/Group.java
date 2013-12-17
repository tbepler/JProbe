package old.datatypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A class used to represent groups of DataTypes. This class enforces id numbering on all DataTypes it contains.
 * 
 * @author Tristan Bepler
 *
 * @param <D>
 */
public class Group<D extends DataType> implements DataType, Iterable<D>{
	
	private List<D> data;
	private int id = 0;
	
	/**
	 * Create a new empty group.
	 */
	public Group(){
		data = new ArrayList<D>();
	}
	
	/**
	 * Create a new group and copy the elements from the collection into this group.
	 * @param data the collection to be copied
	 */
	public Group(Collection<D> data){
		this.data = new ArrayList<D>(data);
		enforceNumbering();
	}
	
	private void enforceNumbering(){
		int num = 1;
		for(D d : data){
			d.setId(num);
			num++;
		}
	}
	
	/**
	 * Adds the given element to the end of this group. The elements id will be updated appropriately.
	 * @param element to be added
	 */
	public void add(D element){
		data.add(element);
		element.setId(data.size());
	}
	
	/**
	 * Adds the given element at the location specified by id. Elements following the inserted element will be renumbered appropriately.
	 * @param id at which element should be inserted
	 * @param element to be inserted
	 */
	public void add(int id, D element){
		data.add(id-1, element);
		enforceNumbering();
	}
	
	/**
	 * Appends elements of the given collection to the end of this group. DataTypes from the collection will be renumbered accordingly.
	 * @param c collection containing elements to be added
	 * @return true if this group changed as a result of the addition
	 */
	public boolean addAll(Collection<? extends D> c){
		if(data.addAll(c)){
			enforceNumbering();
			return true;
		}
		return false;
	}
	
	/**
	 * Appends elements of the given collection starting at the specified id. Elements of this group will be renumbered accordingly.
	 * @param id location to start element addition
	 * @param c collection containing elements to be added
	 * @return true if this group changed as a result of the addition
	 */
	public boolean addAll(int id, Collection<? extends D> c){
		if(data.addAll(id-1, c)){
			enforceNumbering();
			return true;
		}
		return false;
	}
	
	/**
	 * Removes all elements from this group.
	 */
	public void clear(){
		data.clear();
	}
	
	/**
	 * Checks if the given element is contained within this group.
	 * @param element to be checked
	 * @return true if the element is contained in this group
	 */
	public boolean contains(D element){
		return data.contains(element);
	}
	
	/**
	 * Returns the DataType contained by this group with the specified id.
	 * @param id of the DataType to be returned
	 * @return DataType with the given id
	 */
	public D get(int id){
		return data.get(id-1);
	}
	
	/**
	 * Returns the list that backs this group. Changes to the list will be reflected in the group and vice versa.
	 * @return the list backing this group.
	 */
	public List<D> getList(){
		return data;
	}
	
	/**
	 * Removes the specified element from this group. This is iteration safe as long as the iterator was obtained using the group's iterator() method.
	 * @param element to be removed
	 * @return true if the element was found
	 */
	public boolean remove(D element){
		if(data.remove(element)){
			enforceNumbering();
			return true;
		}
		return false;
	}
	
	/**
	 * Removes the element from this group with the specified id. This is iteration safe as long as the iterator was obtained using the group's iterator() method.
	 * @param id of the element to be removed
	 * @return the removed element
	 */
	public D remove(int id){
		D removed = data.remove(id-1);
		enforceNumbering();
		return removed;
	}
	
	/**
	 * Replaces the element with the given Id in this group with the specified element.
	 * @param id of element to be replaced
	 * @param element new element
	 * @return the replaced element
	 */
	public D set(int id, D element){
		element.setId(id);
		return data.set(id-1, element);
	}
	
	/**
	 * Returns the number of elements this group contains.
	 * @return the number of elements contained by this group
	 */
	public int size(){
		return data.size();
	}
	
	/**
	 * Sorts this group using the given comparator. Elements will be renumbered accordingly.
	 * @param c comparator on which this group should be sorted
	 */
	public void sort(Comparator<? super D> c){
		Collections.sort(data, c);
		enforceNumbering();
	}
	
	/**
	 * Returns an iterator over the elements of this group.
	 */
	@Override
	public Iterator<D> iterator() {
		return new ArrayList<D>(data).iterator();
	}
	
	/**
	 * Return the id of this group.
	 */
	@Override
	public int getId() {
		return id;
	}
	
	/**
	 * Set the id of this group.
	 */
	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
