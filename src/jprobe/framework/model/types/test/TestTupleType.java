package jprobe.framework.model.types.test;

import java.util.Arrays;
import java.util.Deque;

import util.ArrayUtils;
import jprobe.framework.model.tuple.Tuple;
import jprobe.framework.model.tuple.Tuples;
import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.Types;

public class TestTupleType extends junit.framework.TestCase{
	
	public void testBoxing(){
		Tuple tuple = Tuples.newTuple("hello", 42, 3.14, "world");
		Type<Tuple> type = Types.typeOf(tuple);
		Object[] box = new Object[]{"hello", 42, 3.14, "world"};
		Type<?>[] boxType = Types.typesOf(box);
		
		assertTrue(type.isBoxable());
		assertTrue(Arrays.equals(box, type.unbox(tuple)));
		
		assertTrue(type.canBox(ArrayUtils.toDeque(boxType)));
		Deque<Object> deck = ArrayUtils.toDeque(box);
		Tuple boxed = type.box(deck);
		assertEquals(tuple, boxed);
		assertTrue(deck.isEmpty());
		
	}
	
}
