package jprobe.framework.model.tuple;

import jprobe.framework.model.types.TupleClass;
import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.Types;

public class Tuple2Type<A,B> extends TupleClass<Tuple2<A,B>>{
	
	public Tuple2Type(Type<? extends A> a, Type<? extends B> b){
		super(a,b);
	}
	
	public Tuple2Type(A a, B b){
		this(Types.typeOf(a), Types.typeOf(b));
	}
	
}
