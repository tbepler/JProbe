package jprobe.framework.model.types.test;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import jprobe.framework.model.tuple.Tuple;
import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.Types;

public class TestObjectType extends junit.framework.TestCase{
	
	private int i;
	private Type<? extends Integer > itype;
	private int j;
	private Type<? extends Integer> jtype;
	
	private Object arrayList;
	private Type<?> arrayListType;
	
	private Object baseObject;
	private Type<?> objectType;
	
	private Tuple tuple1;
	private Type<?> tuple1Type;
	
	private Tuple tuple2;
	private Type<?> tuple2Type;
	
	private Tuple nestedTuple;
	private Type<?> nestedTupleType;
	
	private static Deque<Object> deque(Object ... objs){
		Deque<Object> deck = new LinkedList<Object>();
		for(Object o : objs){
			deck.add(o);
		}
		return deck;
	}
	
	private static Deque<Type<?>> deque(Type<?> ... types){
		Deque<Type<?>> deck = new LinkedList<Type<?>>();
		for(Type<?> t : types){
			deck.add(t);
		}
		return deck;
	}
	
	@Override
	public void setUp(){
		i = 5;
		itype = Types.typeOf(i);
		
		j = -124;
		jtype = Types.typeOf(j);
		
		arrayList = new ArrayList<Object>();
		arrayListType = Types.typeOf(arrayList);
		
		baseObject = new Object();
		objectType = Types.asObjectType(Object.class);
		
		tuple1 = new Tuple(i);
		tuple1Type = Types.typeOf(tuple1);
		
		tuple2 = new Tuple(i,j);
		tuple2Type = Types.typeOf(tuple2);
		
		nestedTuple = new Tuple(new Tuple(tuple1));
		nestedTupleType = Types.typeOf(nestedTuple);
	}

	public void testExtractTuple(){
		Deque<Object> deck = deque(tuple1);
		int extracted = itype.extract(deck);
		assertEquals(i, extracted);
		assertTrue(deck.isEmpty());
		
		deck.push(nestedTuple);
		Object obj = objectType.extract(deck);
		assertEquals(i, obj);
		assertTrue(deck.isEmpty());
		
		deck.push(tuple2);
		boolean error = false;
		try{
			itype.extract(deck);
		}catch(Throwable t){
			error = true;
		}
		assertTrue(error);
		assertEquals(1, deck.size());
	}
	
	public void testCanExtractTuple(){
		Deque<Object> deck = deque(tuple1);
		assertTrue(itype.canExtract(deck));
		assertFalse(arrayListType.canExtract(deck));
		assertTrue(objectType.canExtract(deck));
		
		deck.clear();
		deck.push(tuple2);
		assertFalse(itype.canExtract(deck));
		assertFalse(objectType.canExtract(deck));
		
		deck.clear();
		deck.push(nestedTuple);
		assertTrue(itype.canExtract(deck));
		assertFalse(arrayListType.canExtract(deck));
		assertTrue(objectType.canExtract(deck));
	}
	
	public void testExtractableFromTuple(){
		Deque<Type<?>> deck = deque(tuple1Type);
		assertTrue(itype.isExtractableFrom(deck));
		assertTrue(deck.isEmpty());
		
		deck.push(tuple2Type);
		assertFalse(itype.isExtractableFrom(deck));
		assertEquals(1, deck.size());
		deck.clear();
		
		deck.push(nestedTupleType);
		assertTrue(itype.isExtractableFrom(deck));
		assertTrue(deck.isEmpty());
	}
	
	public void testExtractObject(){
		Deque<Object> deck = deque(j,i,arrayList,baseObject);
		itype.extract(deck);
		assertEquals(3, deck.size());
		objectType.extract(deck);
		assertEquals(2, deck.size());
		boolean error = false;
		try{
			itype.extract(deck);
		}catch(Throwable t){
			error = true;
		}
		assertTrue(error);
		assertEquals(2, deck.size());
		arrayListType.extract(deck);
		assertEquals(1, deck.size());
		error = false;
		try{
			arrayListType.extract(deck);
		}catch(Throwable t){
			error = true;
		}
		assertTrue(error);
		assertEquals(1, deck.size());
		objectType.extract(deck);
		assertEquals(0, deck.size());
		error = false;
		try{
			jtype.extract(deck);
		}catch(Throwable t){
			error = true;
		}
		assertTrue(error);
	
	}
	
	public void testCanExtractObject(){
		Deque<Object> deck = deque(j);
		assertTrue(itype.canExtract(deck));
		assertFalse(arrayListType.canExtract(deck));
		assertTrue(objectType.canExtract(deck));
		
		deck.push(baseObject);
		assertFalse(itype.canExtract(deck));
		assertFalse(arrayListType.canExtract(deck));
		assertTrue(objectType.canExtract(deck));
		
	}
	
	public void testExtractableFromObject(){
		Deque<Type<?>> deck = deque(jtype);
		assertTrue(itype.isExtractableFrom(deck));
		assertTrue(deck.isEmpty());
		
		deck.push(itype);
		assertFalse(arrayListType.isExtractableFrom(deck));
		assertEquals(1, deck.size());
		deck.clear();
		
		deck.push(arrayListType);
		assertTrue(objectType.isExtractableFrom(deck));
		assertTrue(deck.isEmpty());
		
	}
	
	public void testCast(){
		itype.cast(i);
		itype.cast(j);
		arrayListType.cast(arrayList);
		objectType.cast(baseObject);
		objectType.cast(i);
		objectType.cast(arrayList);
		
		boolean error = false;
		try{
			itype.cast(arrayList);
		}catch(Throwable t){
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try{
			arrayListType.cast(baseObject);
		}catch(Throwable t){
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try{
			arrayListType.cast(i);
		}catch(Throwable t){
			error = true;
		}
		assertTrue(error);
	}
	
	public void testIsInstance(){
		assertTrue(objectType.isInstance(baseObject));
		assertTrue(objectType.isInstance(arrayList));
		assertTrue(objectType.isInstance(i));
		assertFalse(arrayListType.isInstance(baseObject));
		assertTrue(arrayListType.isInstance(arrayList));
		assertFalse(arrayListType.isInstance(i));
		assertFalse(itype.isInstance(baseObject));
		assertFalse(itype.isInstance(arrayList));
		assertTrue(itype.isInstance(i));
	}
	
	public void testAssignableFrom(){
		assertTrue(itype.isAssignableFrom(jtype));
		assertTrue(jtype.isAssignableFrom(itype));
		assertFalse(itype.isAssignableFrom(arrayListType));
		assertFalse(arrayListType.isAssignableFrom(itype));
		//test inheritance assignment
		assertTrue(objectType.isAssignableFrom(arrayListType));
		assertFalse(arrayListType.isAssignableFrom(objectType));
		assertTrue(objectType.isAssignableFrom(itype));
		assertFalse(itype.isAssignableFrom(objectType));
	}
	
}
