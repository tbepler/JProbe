package jprobe.framework.model.compiler.grammar.implementation;

import java.util.Arrays;

public class Equals {
	
	public static boolean equals(Object o1, Object o2){
		if(o1 == o2) return true;
		if(o1 == null || o2 == null) return false;
		return o1.equals(o2);
	}
	
	public static int hashCode(Object ... o){
		return Arrays.deepHashCode(o);
	}
	
}
