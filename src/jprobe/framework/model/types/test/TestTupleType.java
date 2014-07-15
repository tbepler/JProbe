package jprobe.framework.model.types.test;

import java.util.Deque;

import util.ArrayUtils;
import jprobe.framework.model.tuple.Tuple;
import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.Types;

public class TestTupleType extends junit.framework.TestCase{
	
	public void testBoxing(){
		Tuple tuple = new Tuple("hello", 42, 3.14, "world");
		Type<? extends Tuple> type = Types.typeOf(tuple);
		Object[] box = new Object[]{"hello", 42, 3.14, "world"};
		Type<?>[] boxType = Types.typesOf(box);
		
		assertTrue(type.isExtractableFrom(ArrayUtils.toDeque(boxType)));
		Deque<Object> deck = ArrayUtils.toDeque(box);
		assertTrue(type.canExtract(deck));
		Tuple extracted = type.extract(deck);
		assertEquals(tuple, extracted);
		assertTrue(deck.isEmpty());
		
	}
	
}
