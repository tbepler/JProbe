package jprobe.framework.model.types.test;

import java.util.ArrayList;
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
