package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {
	
	public static <T> T head(T ... array){
		return array[0];
	}
	
	public static <T> T[] tail(T ... array){
		return tail(1, array);
	}
	
	public static <T> T[] tail(int headLength, T ... array){
		if(array.length < headLength){
			return (T[]) new Object[]{};
		}
		return Arrays.copyOfRange(array, headLength, array.length);
	}
	
	public static <T> List<T> toList(T[] array){
		if(array != null){
			List<T> list;
			if(array.length > 10){
				list = new ArrayList<T>(array.length);
			}else{
				list = new ArrayList<T>();
			}
			for(T elem : array){
				list.add(elem);
			}
			return list;
		}
		return new ArrayList<T>();
	}
	
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
