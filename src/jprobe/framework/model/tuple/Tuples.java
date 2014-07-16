package jprobe.framework.model.tuple;

import jprobe.framework.model.types.TupleClass;
import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.Types;

public class Tuples {
	
	public static Tuple newTuple(Object ... objs){
		switch(objs.length){
		case 0: return null;
		case 1: /* TODO */ return null;
		case 2: return newTuple(objs[0],objs[1]);
		case 3: return newTuple(objs[0],objs[1],objs[2]);
		default: return new Tuple(objs);
		}
	}
	
	public static TupleClass<? extends Tuple> newTupleType(Type<?> ... types){
		switch(types.length){
		case 0: return null;
		case 1: /* TODO */ return null;
		case 2: return newTupleType(types[0], types[1]);
		case 3: return newTupleType(types[0], types[1], types[2]);
		default: return new TupleClass<Tuple>(types);
		}
	}
	
	public static TupleClass<? extends Tuple> newTupleType(Object ... objs){
		return newTupleType(Types.typesOf(objs));
	}
	
	public static <A,B> Tuple2<A,B> newTuple(A a, B b){
		return new Tuple2<A,B>(a,b);
	}
	
	public static <A,B> Tuple2Type<A,B> newTupleType(Type<? extends A> a, Type<? extends B> b){
		return new Tuple2Type<A,B>(a, b);
	}
	
	public static <A,B> Tuple2Type<A,B> newTupleType(A a, B b){
		return newTupleType(Types.typeOf(a), Types.typeOf(b));
	}
	
	public static <A,B,C> Tuple3<A,B,C> newTuple(A a, B b, C c){
		return new Tuple3<A,B,C>(a,b,c);
	}
	
	public static <A,B,C> Tuple3Type<A,B,C> newTupleType(Type<? extends A> a, Type<? extends B> b, Type<? extends C> c){
		return new Tuple3Type<A,B,C>(a,b,c);
	}
	
	public static <A,B,C> Tuple3Type<A,B,C> newTupleType(A a, B b, C c){
		return newTupleType(Types.typeOf(a), Types.typeOf(b), Types.typeOf(c));
	}
	
}
