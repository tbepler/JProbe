package plugins.jprobe.gui.utils;

import java.util.Collection;

public interface Searchable<E> {
	
	public Collection<E> search(String value);
	public Collection<E> sort(Collection<E> unsorted);
	
}
