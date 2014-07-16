package jprobe.framework.model.tuple;

import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.Types;

public class Tuple2Type<A,B> extends TupleType<Tuple2<A,B>>{
	private static final long serialVersionUID = 1L;

	public Tuple2Type(Type<? extends A> a, Type<? extends B> b){
		super(a,b);
	}
	
	public Tuple2Type(A a, B b){
		this(Types.typeOf(a), Types.typeOf(b));
	}
	
	public Type<? extends A> first(){
		return this.get(0);
	}
	
	public Type<? extends B> second(){
		return this.get(1);
	}

	@Override
	public Tuple2<A, B> newInstance(Object... objs) {
		return new Tuple2<A,B>(first().cast(objs[0]), second().cast(objs[1]));
	}
	
}
