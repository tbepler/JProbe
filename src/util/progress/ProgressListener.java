package util.progress;


public interface ProgressListener {
	
	public static final int MAX_PERCENT = Progress.MAX_PERCENT;
	public static final int MIN_PERCENT = Progress.MIN_PERCENT;
	
	/**
	 * This method is called by the observed class or routine to indicate
	 * that execution has started.
	 * @param message - a start message, can be null
	 */
	public void onStart(String message);
	
	/**
	 * This method is called by the observed class or routine to indicate
	 * that an error has occurred. If this method is called for an unrecoverable
	 * error, then the {@link #onCompletion(String)} method must still be called
	 * after this method.
	 * @param t - {@link Throwable} indicating the error
	 */
	public void onError(Throwable t);
	
	/**
	 * This method is called by the observed class or routine to indicate
	 * that execution has completed.
	 * @param message - a completion message, can be null
	 */
	public void onCompletion(String message);
	
	/**
	 * This method is called by the observed class or routine to indicate
	 * that the execution progress has changed.
	 * @param percent - a value between 0 and 100 (inclusive) indicating
	 * the percentage completion. Any other value indicates that the observed
	 * progress is indeterminate. This can be checked using the {@link
	 * Progress#isIndeterminate(int)} method.
	 * @param message - a progress message, can be null. A value of null
	 * indicates not to change the previous message, if any.
	 */
	public void progressUpdate(int percent, String message);
	
}
