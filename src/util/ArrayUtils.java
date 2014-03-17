package util;

public class ArrayUtils {
	
	public static int min(int[] ints){
		int min = Integer.MAX_VALUE;
		for(int i : ints){
			if(i < min){
				min = i;
			}
		}
		return min;
	}
	
	public static int max(int[] ints){
		int max = Integer.MIN_VALUE;
		for(int i : ints){
			if(i > max){
				max = i;
			}
		}
		return max;
	}
	
	public static boolean contains(int[] ints, int value){
		for(int i : ints){
			if(i == value) return true;
		}
		return false;
	}
	
}
