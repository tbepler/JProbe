package jprobe.framework.model.tuple;

import jprobe.framework.model.types.Type;

public class Tuple4<A,B,C,D> extends Tuple {
	private static final long serialVersionUID = 1L;
	
	private final Tuple4Type<A,B,C,D> m_Type;
	
	public Tuple4(A a, B b, C c, D d) {
		super(a,b,c,d);
		m_Type = Tuples.newTupleType(a,b,c,d);
	}
	
	Tuple4(Tuple4Type<A,B,C,D> type, A a, B b, C c, D d){
		super(a,b,c,d);
		m_Type = type;
	}

	@Override
	public Type<? extends Tuple> getType() {
		return m_Type;
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
	
	public D fourth(){
		return this.get(3);
	}

}
