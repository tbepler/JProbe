package util.concurrent;

import java.util.concurrent.ExecutionException;

/**
 * Shamelessly ripped off from Java 8
 * @author Tristan Bepler
 *
 * @param <T>
 */
public interface Result<T> {
	
	public T get() throws ExecutionException;
	
}
