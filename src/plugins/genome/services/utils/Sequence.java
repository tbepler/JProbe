package plugins.genome.services.utils;

import java.io.Serializable;

public interface Sequence<E, I> extends Serializable, Comparable<E>, Iterable<E>{
	
	public E getElementAt(I index);
	
	public Sequence<E, I> subsequence(I startIndex);
	public Sequence<E, I> subsequence(I startIndex, I endIndex);
	public Sequence<E, I>[] split(I index);
	public Sequence<E, I>[] split(Sequence<E, I> match);
	public Sequence<E, I> join(Sequence<E, I> other);
	public Sequence<E, I> insert(Sequence<E, I> other, I index);
	public Sequence<E, I> insert(E element, I index);
	public Sequence<E, I> removeElementAt(I index);
	public Sequence<E, I> remove(E element);
	public Sequence<E, I> remove(Sequence<E, I> other);
	public boolean matches(Sequence<E, I> other);
	public boolean contains(Sequence<E, I> other);
	public boolean contains(E element);
	public int size();
	
	
}
