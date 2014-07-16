package jprobe.framework.model.tuple;

import jprobe.framework.model.types.Type;

public class Tuple3<A,B,C> extends Tuple{
	private static final long serialVersionUID = 1L;
	
	private final Tuple3Type<A,B,C> m_Type;
	
	public Tuple3(A first, B second, C third){
		super(first, second, third);
		m_Type = Tuples.newTupleType(first, second, third);
	}
	
	Tuple3(Tuple3Type<A,B,C> type, A a, B b, C c){
		super(a,b,c);
		m_Type = type;
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

	@Override
	public Type<? extends Tuple> getType() {
		return m_Type;
	}
	
}
