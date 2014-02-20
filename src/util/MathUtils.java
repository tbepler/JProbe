package util;

/**
 * This class provides math utility functions.
 * @author Tristan Bepler
 *
 */

public class MathUtils {
	
	/**
	 * Clamps the value between the min and max. In other words, returns min if (val{@literal <}min), returns max if (val>max),
	 * and returns val otherwise.
	 * @param val value to be clamped
	 * @param min minumum value
	 * @param max maximum value
	 * @return a value v that adheres to (min<=v<=max)
	 */
	public static short clamp(short val, short min, short max){
		if(val>max) return max;
		if(val<min) return min;
		return val;
	}
	
	/**
	 * Clamps the value between the min and max. In other words, returns min if (val{@literal <}min), returns max if (val>max),
	 * and returns val otherwise.
	 * @param val value to be clamped
	 * @param min minumum value
	 * @param max maximum value
	 * @return a value v that adheres to (min<=v<=max)
	 */
	public static int clamp(int val, int min, int max){
		if(val>max) return max;
		if(val<min) return min;
		return val;
	}
	
	/**
	 * Clamps the value between the min and max. In other words, returns min if (val{@literal <}min), returns max if (val>max),
	 * and returns val otherwise.
	 * @param val value to be clamped
	 * @param min minumum value
	 * @param max maximum value
	 * @return a value v that adheres to (min<=v<=max)
	 */
	public static float clamp(float val, float min, float max){
		if(val>max) return max;
		if(val<min) return min;
		return val;
	}
	
	/**
	 * Clamps the value between the min and max. In other words, returns min if (val{@literal <}min), returns max if (val>max),
	 * and returns val otherwise.
	 * @param val value to be clamped
	 * @param min minumum value
	 * @param max maximum value
	 * @return a value v that adheres to (min<=v<=max)
	 */
	public static double clamp(double val, double min, double max){
		if(val>max) return max;
		if(val<min) return min;
		return val;
	}
	
	
}
