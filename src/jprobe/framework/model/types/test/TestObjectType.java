package jprobe.framework.model.types.test;

import java.util.ArrayList;
import java.util.Deque;

import util.ArrayUtils;
import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.Types;

public class TestObjectType extends junit.framework.TestCase{
	
	public void testCast(){
		Integer i = 5;
		Type<? extends Integer> iType = Types.typeOf(i);
		
		Integer j = 13;
		Type<? extends Integer> jType = Types.typeOf(j);
		
		assertTrue(jType.isAssignableFrom(iType));
		assertTrue(iType.isAssignableFrom(jType));
		
		Object obj = new Integer(8);
		assertTrue(iType.isInstance(obj));
		assertTrue(jType.isInstance(obj));
		
		Integer cast = iType.cast(obj);
		assertEquals(cast, obj);
		cast = jType.cast(obj);
		assertEquals(cast, obj);
		
		Object notInt = new ArrayList<Object>();
		Type<?> notIntType = Types.typeOf(notInt);
		
		assertFalse(notIntType.isAssignableFrom(iType));
		assertFalse(iType.isAssignableFrom(notIntType));
		
		assertFalse(iType.isInstance(notInt));
		assertFalse(notIntType.isInstance(i));
		
		boolean error = false;
		try{
			Integer fail = iType.cast(notInt);
		}catch(Throwable t){
			error = true;
		}
		assertTrue(error);
	}
	
	public void testExtractObject(){
		int i = 5;
		int j = 8;
		Object obj = j;
		Type<? extends Integer> type = Types.typeOf(i);
		Type<?> otherType = Types.typeOf(obj);
		
		Deque<Type<?>> deck = ArrayUtils.<Type<?>>toDeque(otherType);
		assertTrue(type.isExtractableFrom(deck));
		assertTrue(deck.isEmpty());
		
		deck.push(type);
		assertTrue(otherType.isExtractableFrom(deck));
		assertTrue(deck.isEmpty());
		
		Deque<Object> objs = ArrayUtils.toDeque(obj);
		int extracted = type.extract(objs);
		assertEquals(j, extracted);
		assertTrue(objs.isEmpty());
		
		obj = new ArrayList<Object>();
		otherType = Types.typeOf(obj);
		
		deck.push(otherType);
		assertFalse(type.isExtractableFrom(deck));
		assertEquals(1, deck.size());
		
		deck.clear();
		deck.push(type);
		assertFalse(otherType.isExtractableFrom(deck));
		assertEquals(1, deck.size());
		
		objs.push(obj);
		boolean error = false;
		try{
			int fail = type.extract(objs);
		}catch(Throwable t){
			error = true;
		}
		assertTrue(error);
		assertEquals(1, objs.size());
		
	}
	
}
