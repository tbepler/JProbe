package jprobe.framework.model.tuple;

import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.Types;

public class Tuples {
	
	public static Tuple newTuple(Object ... objs){
		return newTupleType(Types.typesOf(objs)).newInstance(objs);
	}
	
	public static TupleType<?> newTupleType(Type<?> ... types){
		switch(types.length){
		case 0: return null;
		case 1: /* TODO */ return null;
		case 2: return newTupleType(types[0], types[1]);
		case 3: return newTupleType(types[0], types[1], types[2]);
		default: return new TupleDefaultType(types);
		}
	}
	
	public static TupleType<?> newTupleType(Object ... objs){
		return newTupleType(Types.typesOf(objs));
	}
	
	public static <A> Tuple1<A> newTuple(A a){
		return new Tuple1<A>(a);
	}
	
	public static <A> Tuple1Type<A> newTupleType(Type<? extends A> a){
		return new Tuple1Type<A>(a);
	}
	
	public static <A> Tuple1Type<A> newTupleType(A a){
		return newTupleType(Types.typeOf(a));
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
	
	public static <A,B,C,D> Tuple4<A,B,C,D> newTuple(A a, B b, C c, D d){
		return new Tuple4<A,B,C,D>(a,b,c,d);
	}
	
	public static <A,B,C,D> Tuple4Type<A,B,C,D> newTupleType(Type<? extends A> a, Type<? extends B> b, Type<? extends C> c, Type<? extends D> d){
		return new Tuple4Type<A,B,C,D>(a,b,c,d);
	}
	
	public static <A,B,C,D> Tuple4Type<A,B,C,D> newTupleType(A a, B b, C c, D d){
		return newTupleType(Types.typeOf(a), Types.typeOf(b), Types.typeOf(c), Types.typeOf(d));
	}
	
}
