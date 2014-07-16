package jprobe.framework.model.tuple;

import jprobe.framework.model.types.Type;

public class Tuple4Type<A,B,C,D> extends TupleType<Tuple4<A,B,C,D>> {
	private static final long serialVersionUID = 1L;

	public Tuple4Type(Type<? extends A> a, Type<? extends B> b, Type<? extends C> c, Type<? extends D> d){
		super(a,b,c,d);
	}
	
	@Override
	public Tuple4<A, B, C, D> newInstance(Object... objs) {
		return new Tuple4<A,B,C,D>(first().cast(objs[0]), second().cast(objs[1]), third().cast(objs[2]), fourth().cast(objs[3]));
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
	
	public Type<? extends D> fourth(){
		return this.get(3);
	}
	
}
