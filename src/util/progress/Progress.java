package util.progress;

public class Progress {
	
	public static final int MAX_PERCENT = 100;
	public static final int MIN_PERCENT = 0;
	
	public static boolean isIndeterminate(int percent){
		return percent >= MIN_PERCENT && percent <= MAX_PERCENT;
	}
	
}
