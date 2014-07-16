package jprobe.framework.model.tuple;

import jprobe.framework.model.types.TupleClass;
import jprobe.framework.model.types.Type;

public class Tuple3Type<A,B,C> extends TupleClass<Tuple3<A,B,C>> {
	
	public Tuple3Type(Type<? extends A> a, Type<? extends B> b, Type<? extends C> c){
		super(a,b,c);
	}
	
}
