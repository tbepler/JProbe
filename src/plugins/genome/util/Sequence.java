package plugins.genome.util;

import java.io.Serializable;

public interface Sequence<E, I> extends Serializable, Comparable<Sequence<E, I>>, Iterable<E>{
	
	public E getElementAt(I index);
	
	public Sequence<E, I> subsequence(I startIndex);
	public Sequence<E, I> subsequence(I startIndex, I endIndex);
	public Sequence<E, I> append(Sequence<E, I> other);
	public Sequence<E, I> insert(Sequence<E, I> other, I index);
	public Sequence<E, I> insert(E element, I index);
	public Sequence<E, I> removeElementAt(I index);
	public Sequence<E, I> remove(E element);
	public Sequence<E, I> remove(Sequence<E, I> other);
	public boolean matches(Sequence<E, I> other);
	public boolean contains(Sequence<E, I> other);
	public boolean contains(E element);
	public int size();
	
	public I getStartIndex();
	public I getEndIndex();
	
	
}
