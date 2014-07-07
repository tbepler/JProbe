package util.concurrent;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class Concurrent {
	
	public <T> void invoke(ExecutorService threads, final Callable<? extends T> task, final Collector<Result<T>> resultsCollector){
		threads.execute(new Runnable(){

			@Override
			public void run() {
				process(task, resultsCollector);
				resultsCollector.flush();
			}
			
		});
	}
	
	public <T> void invoke(ExecutorService threads, final Task<T> task){
		threads.execute(task);
	}
	
	public <T> void invokeAll(ExecutorService threads, final Collection<? extends Callable<? extends T>> tasks, final Collector<Result<T>> resultsCollector){
		for(final Callable<? extends T> task : tasks){
			threads.execute(new Runnable(){

				@Override
				public void run() {
					process(task, resultsCollector);
					resultsCollector.flush();
				}
				
			});
		}
	}
	
	public <T> void invokeAll(ExecutorService threads, final Collection<? extends Task<T>> tasks){
		for(final Task<T> t : tasks){
			threads.execute(t);
		}
	}
	
	private <T> void process(final Callable<? extends T> task, final Collector<Result<T>> resultsCollector){
		Result<T> result;
		try {
			final T val = task.call();
			result = newResult(val);
		} catch (final Exception e) {
			result = newResult(e, null);
		}
		resultsCollector.add(result);
	}
	
	private <T> Result<T> newResult(final T value){
		return new Result<T>(){

			@Override
			public T get() throws ExecutionException {
				return value;
			}
			
		};
	}
	
	private <T> Result<T> newResult(final Exception e, T type){
		return new Result<T>(){

			@Override
			public T get() throws ExecutionException {
				throw new ExecutionException(e);
			}
			
		};
	}
	
}
