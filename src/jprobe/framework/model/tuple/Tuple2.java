package jprobe.framework.model.tuple;

public class Tuple2<A,B> extends Tuple{
	private static final long serialVersionUID = 1L;
	
	private final Tuple2Type<A,B> m_Type;
	
	public Tuple2(A first, B second){
		super(first, second);
		m_Type = Tuples.newTupleType(first, second);
	}
	
	Tuple2(Tuple2Type<A,B> type, A a, B b){
		super(a,b);
		m_Type = type;
	}
	
	public A first(){
		return this.get(0);
	}
	
	public B second(){
		return this.get(1);
	}
	
	@Override
	public Tuple2Type<A,B> getType(){
		return m_Type;
	}
	
	

}
