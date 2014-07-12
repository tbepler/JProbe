package jprobe.framework.model.tuple;

public class Tuple3<A,B,C> extends Tuple{
	private static final long serialVersionUID = 1L;
	
	public final A first;
	public final B second;
	public final C third;
	
	public Tuple3(A first, B second, C third){
		super(first, second, third);
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	public Tuple3(Tuple2<A,B> tuple2, C third){
		this(tuple2.first, tuple2.second, third);
	}
	
	public Tuple3(A first, Tuple2<B,C> tuple2){
		this(first, tuple2.first, tuple2.second);
	}
	
}
