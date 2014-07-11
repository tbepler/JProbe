package util.tuple;

import java.util.Arrays;

public class Tuple3<A,B,C> {
	
	public final A first;
	public final B second;
	public final C third;
	private final int hash;
	
	public Tuple3(A first, B second, C third){
		this.first = first;
		this.second = second;
		this.third = third;
		hash = Arrays.hashCode(new Object[]{first, second, third});
	}
	
	public Tuple3(Tuple2<A,B> tuple2, C third){
		this(tuple2.first, tuple2.second, third);
	}
	
	public Tuple3(A first, Tuple2<B,C> tuple2){
		this(first, tuple2.first, tuple2.second);
	}
	
	@Override
	public String toString(){
		return "("+first+", "+second+", "+third+")";
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof Tuple3){
			Tuple3<?,?,?> other = (Tuple3<?,?,?>) o;
			return equals(first, other.first) && equals(second, other.second) && equals(third, other.third);
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
