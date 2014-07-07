package util.concurrent;

public abstract class Task<T> implements Runnable{
	
	private Collector<? extends Result<T>> collector;
	
	public Task(Collector<? extends Result<T>> collector){
		this.collector = collector;
	}
	
	@Override
	public void run() {
		this.execute(collector);
	}
	
	public abstract void execute(Collector<? extends Result<T>> collector);

}
