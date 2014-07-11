package util.tuple;

import java.util.Arrays;

public class Tuple2<A,B> {
	
	public final A first;
	public final B second;
	private final int hash;
	
	public Tuple2(A first, B second){
		this.first = first;
		this.second = second;
		hash = Arrays.hashCode(new Object[]{first, second});
	}
	
	@Override
	public String toString(){
		return "("+first+", "+second+")";
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Tuple2){
			Tuple2<?,?> other = (Tuple2<?,?>) o;
			return equals(first, other.first) && equals(second, other.second);
		}
		return false;
	}
	
	private static <T> boolean equals(T a, T b){
		if(a == b) return true;
		if(a != null){
			return a.equals(b);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return hash;
	}

}
