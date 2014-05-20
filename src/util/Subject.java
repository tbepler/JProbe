package util;

public interface Subject<T> {

	public void register(Observer<T> obs);
	public void unregister(Observer<T> obs);
	
}
