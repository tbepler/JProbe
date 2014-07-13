package jprobe.framework.model.tuple;

public class Tuple2<A,B> extends Tuple{
	private static final long serialVersionUID = 1L;
	
	public Tuple2(A first, B second){
		super(first, second);
	}
	
	public A first(){
		return this.get(0);
	}
	
	public B second(){
		return this.get(1);
	}
	
	

}
