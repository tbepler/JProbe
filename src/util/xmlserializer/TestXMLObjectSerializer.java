package util.xmlserializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.*;

import org.xml.sax.SAXException;

import util.xmlserializer.XMLSerializerUtil.ReadField;
import util.xmlserializer.XMLSerializerUtil.WriteField;

/**
 * Some test cases for the XMLSerializer
 * 
 * @author Tristan Bepler
 *
 */

public class TestXMLObjectSerializer extends junit.framework.TestCase{
	
	
	public static class TestParent implements Serializable{
		public String foo = "helloworld";
	}
	
	public static class TestChild extends TestParent{
		public int number = 10;
	}
	
	public static class TestCycle implements Serializable{
		public String contents;
		public TestCycle next;
	}
	
	public static class TestIgnore implements Serializable{
		public String stuff = "stuff";
		public int num = 150;
		@XMLIgnore
		public Method method = TestIgnore.class.getMethods()[0];
		public String[] test = new String[]{"did", "ignore", "work"};
		
		public void iHaveAMethod(){
			System.out.println("method here.");
		}
	}
	
	public static class TestReadWriteOverride extends TestParent{
		public String val = "entry";
		public String other = "hello world";
		public int stuff = 150;
		private void readObjectXML(ReadField reader) throws ObjectReadException{
			System.out.println("readOjectXML called");
			val = (String) reader.read("a", null);
			other = (String) reader.read("b", null);
			stuff = reader.read("c", 0);
		}
		private void writeObjectXML(WriteField writer) throws ObjectWriteException{
			System.out.println("writeObjectXML called");
			writer.write("a", val);
			writer.write("b", other);
			writer.write("c", stuff);
		}
	}
	
	public static class TestOverrideChild extends TestReadWriteOverride{
		String remember = "important stuff";
		String forget = "who cares?";
		private void readObjectXML(ReadField reader) throws ObjectReadException{
			System.out.println("readOjectXML called2");
			remember = (String) reader.read("important", null);
			forget = (String) reader.read("whatever", null);
		}
		private void writeObjectXML(WriteField writer) throws ObjectWriteException{
			System.out.println("writeObjectXML called2");
			writer.write("important", remember);
		}
	}
	
	public static class TestNonSerialDefault{
		String test;
		public TestNonSerialDefault(){
			test = "initialized";
		}
	}
	
	public static class TestNonSerialNoDefault{
		String test;
		public TestNonSerialNoDefault(String error){
			test = error;
		}
	}
	
	public static class TestSerialExtendsDefault extends TestNonSerialDefault implements Serializable{
		String contents;
		public TestSerialExtendsDefault(String contents){
			this.contents = contents;
		}
	}
	
	public static class TestSerialExtendsNoDefault extends TestNonSerialNoDefault implements Serializable{
		String fail = "I should have a default superclass :(";
		public TestSerialExtendsNoDefault(String error) {
			super(error);
		}
	}
	
	
	public void testCollection() throws ObjectWriteException, ObjectReadException{
		
		Collection<Integer> write = new ArrayList<Integer>();
		write.add(1);
		write.add(2);
		
		XMLSerializerUtil.write(write, "test.xml");
		
		Collection<Integer> read = (Collection<Integer>) XMLSerializerUtil.read("test.xml");
		assertEquals(write.size(), read.size());
		assertTrue(write.containsAll(read));
		assertTrue(read.containsAll(write));
	}
	
	public void testHashMap() throws ObjectReadException, ObjectWriteException{
		
		Map<String, Integer> write2 = new HashMap<String, Integer>();
		write2.put("test", 1);
		write2.put("huh", 500);
		write2.put("foo", 35235);
		write2.put("hello", 2);
		write2.put("null", null);
		write2.put(null, 1000);
		
		XMLSerializerUtil.write(write2, "test2.xml");
		
		Map<String, Integer> read2 = (Map<String, Integer>) XMLSerializerUtil.read("test2.xml");
		
		assertEquals(write2.size(), read2.size());
		assertEquals(write2.get("test"), read2.get("test"));
		assertEquals(write2.get("huh"), read2.get("huh"));
		assertEquals(write2.get("foo"), read2.get("foo"));
		assertEquals(write2.get("hello"), read2.get("hello"));
		assertEquals(write2.get("null"), read2.get("null"));
		assertEquals(write2.get(null), read2.get(null));
	}
	
	public void testParentChild() throws ObjectReadException, ObjectWriteException{
		
		TestChild t = new TestXMLObjectSerializer.TestChild();
		XMLSerializerUtil.write(t, "test3.xml");
		TestChild r = (TestChild) XMLSerializerUtil.read("test3.xml");
		assertEquals(t.foo, r.foo);
		assertEquals(t.number, r.number);
		
	}
	
	public void testCycles() throws ObjectReadException, ObjectWriteException{
		TestCycle cyc = new TestXMLObjectSerializer.TestCycle();
		cyc.contents = "one";
		TestCycle cyc2 = new TestXMLObjectSerializer.TestCycle();
		cyc2.contents = "two";
		cyc.next = cyc2;
		cyc2.next = cyc;
		
		XMLSerializerUtil.write(cyc, "test4.xml");
		TestCycle cycRead = (TestCycle) XMLSerializerUtil.read("test4.xml");
		assertEquals(cyc.contents, cycRead.contents);
		assertEquals(cyc2.contents, cycRead.next.contents);
		assertEquals(cycRead.next.next.contents, cyc.contents);
	}
	
	public void testIgnore() throws ObjectReadException, ObjectWriteException{
		
		TestIgnore ig = new TestIgnore();
		XMLSerializerUtil.write(ig, "test5.xml");
		TestIgnore igRead = (TestIgnore) XMLSerializerUtil.read("test5.xml");
		assertEquals(ig.num, igRead.num);
		assertEquals(ig.stuff, igRead.stuff);
		for(int i=0; i<ig.test.length; i++){
			assertEquals(ig.test[i], igRead.test[i]);
		}
		assertNull(igRead.method);
	}
	
	public void testComplexMap() throws ObjectReadException, ObjectWriteException{
		
		Map<String, Object> testTree = new TreeMap<String, Object>();
		String[] value1 = new String[]{"hi", "there"};
		String key1 = "asaadsfad";
		testTree.put(key1, value1);
		String key2 = "1.67";
		TestCycle value2 = new TestCycle();
		value2.contents = "value2";
		testTree.put(key2, value2);
		String key3 = "25550";
		Map<String, Object> value3 = testTree;
		testTree.put(key3, value3);
		String key4 = "key4";
		TestCycle value4 = new TestCycle();
		value4.contents = "asaadsfad";
		value2.next = value4;
		value4.next = value2;
		testTree.put(key4, value4);
		
		XMLSerializerUtil.write(testTree, "test6.xml");
		
		Map<Object, Object> readTree = (Map<Object, Object>) XMLSerializerUtil.read("test6.xml");
		assertEquals(testTree.getClass(), readTree.getClass());
		assertEquals(testTree.size(), readTree.size());
		assertEquals(Array.get(testTree.get(key1), 0), Array.get(readTree.get(key1), 0));
		assertEquals(Array.get(testTree.get(key1), 1), Array.get(readTree.get(key1), 1));
		assertEquals(((TestCycle) testTree.get(key2)).next, testTree.get(key4));
		assertEquals(((TestCycle) readTree.get(key2)).next, readTree.get(key4));
		assertTrue(testTree.keySet().containsAll(readTree.keySet()));
		assertTrue(readTree.keySet().containsAll(testTree.keySet()));
		assertEquals(readTree, readTree.get(key3));
	}
	
	public void testSerializeDeserialize() throws IOException, ObjectWriteException, ObjectReadException{
		
		Collection<String> test = new HashSet<String>();
		test.add("this");
		test.add("is");
		test.add("a");
		test.add("test");
		
		XMLSerializerUtil.serialize(test, new BufferedOutputStream(new FileOutputStream("test7.xml")));
		
		Collection<String> in = (Collection<String>) XMLSerializerUtil.deserialize(new BufferedInputStream(new FileInputStream("test7.xml")));
		
		assertEquals(test.size(), in.size());
		assertEquals(test.getClass(), in.getClass());
		assertTrue(test.containsAll(in));
		assertTrue(in.containsAll(test));
	}
	
	public void testCustomReadWrite() throws ObjectWriteException, ObjectReadException{
		TestReadWriteOverride test = new TestReadWriteOverride();
		XMLSerializerUtil.write(test, "test8.xml");
		TestReadWriteOverride read = (TestReadWriteOverride) XMLSerializerUtil.read("test8.xml");
		assertEquals(test.val, read.val);
		assertEquals(test.other, read.other);
		assertEquals(test.stuff, read.stuff);
	
	}
	
	public void testCustomReadWriteWithInheritance() throws ObjectWriteException, ObjectReadException{
		TestOverrideChild test = new TestOverrideChild();
		XMLSerializerUtil.write(test, "test9.xml");
		TestOverrideChild read = (TestOverrideChild) XMLSerializerUtil.read("test9.xml");
		assertEquals(test.foo, read.foo);
		assertEquals(test.other, read.other);
		assertEquals(test.remember, read.remember);
		assertEquals(test.stuff, read.stuff);
		assertEquals(test.val, read.val);
		assertNull(read.forget);
		assertEquals(test.forget, "who cares?");
	}
	
	public void testSerialNonSerial() throws ObjectWriteException, ObjectReadException{
		TestSerialExtendsDefault t = new TestSerialExtendsDefault("42");
		//Notice that the contents of the superclass are not written
		XMLSerializerUtil.write(t, "test10.xml");
		TestSerialExtendsDefault r = (TestSerialExtendsDefault) XMLSerializerUtil.read("test10.xml");
		assertEquals(t.contents, r.contents);
		assertEquals(t.test, r.test);
		
		TestSerialExtendsNoDefault f = new TestSerialExtendsNoDefault("Wow I have a lot of tests in here.");
		boolean err = false;
		try{
			//The superclass has no default constructor, so this fails
			XMLSerializerUtil.write(f, "test11.xml");
		} catch (ObjectWriteException e){
			err = true;
		}
		assertTrue(err);
	}
	
}
