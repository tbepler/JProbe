package jprobe.framework.model.tuple;

public class Tuple2<A,B> extends Tuple{
	private static final long serialVersionUID = 1L;
	
	public final A first;
	public final B second;
	
	public Tuple2(A first, B second){
		super(first, second);
		this.first = first;
		this.second = second;
	}
	
	

}
