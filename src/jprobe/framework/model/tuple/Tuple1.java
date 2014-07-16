package jprobe.framework.model.tuple;

public class Tuple1<A> extends Tuple{
	private static final long serialVersionUID = 1L;

	private final Tuple1Type<A> m_Type;
	
	public Tuple1(A a){
		super(a);
		m_Type = Tuples.newTupleType(a);
	}
	
	Tuple1(Tuple1Type<A> type, A a){
		super(a);
		m_Type = type;
	}
	
	@Override
	public Tuple1Type<A> getType() {
		return m_Type;
	}
	
	public A first(){
		return this.get(0);
	}

}
