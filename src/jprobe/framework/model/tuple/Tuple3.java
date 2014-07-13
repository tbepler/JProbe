package jprobe.framework.model.tuple;

public class Tuple3<A,B,C> extends Tuple{
	private static final long serialVersionUID = 1L;
	
	public Tuple3(A first, B second, C third){
		super(first, second, third);
	}
	
	public Tuple3(Tuple2<A,B> tuple2, C third){
		this(tuple2.first(), tuple2.second(), third);
	}
	
	public Tuple3(A first, Tuple2<B,C> tuple2){
		this(first, tuple2.first(), tuple2.second());
	}
	
	public A first(){
		return this.get(0);
	}
	
	public B second(){
		return this.get(1);
	}
	
	public C third(){
		return this.get(2);
	}
	
}
