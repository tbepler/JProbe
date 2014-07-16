package jprobe.framework.model.tuple;

import jprobe.framework.model.types.Type;

public class Tuple3Type<A,B,C> extends TupleType<Tuple3<A,B,C>> {
	private static final long serialVersionUID = 1L;

	public Tuple3Type(Type<? extends A> a, Type<? extends B> b, Type<? extends C> c){
		super(a,b,c);
	}
	
	public Type<? extends A> first(){
		return this.get(0);
	}
	
	public Type<? extends B> second(){
		return this.get(1);
	}
	
	public Type<? extends C> third(){
		return this.get(2);
	}

	@Override
	public Tuple3<A, B, C> newInstance(Object... objs) {
		return new Tuple3<A,B,C>(first().cast(objs[0]), second().cast(objs[1]), third().cast(objs[2]));
	}
	
}
