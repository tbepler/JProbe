package util.observer;


public interface Observer<T> {
	
	public void update(Subject<T> observed, T notification);
	
}
