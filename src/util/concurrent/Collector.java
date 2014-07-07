package util.concurrent;

/**
 * Shamelessly ripped off from Java 8
 * @author Tristan Bepler
 *
 * @param <T>
 */
public interface Collector<T> {
	
	public void add(T result);
	
	public void flush();
	
}
