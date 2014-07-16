package jprobe.framework.model.tuple;

import jprobe.framework.model.types.Type;

public class Tuple1Type<A> extends TupleType<Tuple1<A>>{
	private static final long serialVersionUID = 1L;
	
	public Tuple1Type(Type<? extends A> a){
		super(a);
	}
	
	@Override
	public Tuple1<A> newInstance(Object... objs) {
		return new Tuple1<A>(this, first().cast(objs[0]));
	}
	
	public Type<? extends A> first(){
		return this.get(0);
	}

}
