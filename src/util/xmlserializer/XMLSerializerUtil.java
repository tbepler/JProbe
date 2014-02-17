package util.xmlserializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 * This class can be used to serialize objects as XML to files or OutputStreams and deserialize objects from those XML
 * files or InputStreams, in a manner similar to how java serialization works. Objects you wish to serialize should implement
 * the {@link Serializable} interface. There are also a few objects that cannot be instantiated due to java's internal security.
 * I included a work around for instantiating Class objects, but there may be others that can not be deserialized properly.
 * Furthermore, if your object contains objects that do not implement Serializable, those objects must have a public or protected
 * default constructor available to initialize their state. I would advise making all contents serializable,
 * not serializing objects with those components, using the @{@link XMLIgnore} annotation to ignore those fields, or defining
 * custom read and write methods for your object. 
 * <p>
 * I have also added support for implementing custom read and write methods to objects. They must follow this exact
 * signature:
 * <p>
 * private void writeObjectXML(WriteField writer) throws ObjectWriteException;
 * <p>
 * private void readObjectXML(ReadField reader) throws ObjectReadException;
 * <p>
 * The write object method is responsible for writing out the fields you wish to save. The read object method 
 * is responsible for restoring the object's state from the xml written by your write method. These methods are only responsible
 * for your object's fields, not any superclass's fields. The Write- and ReadField objects provide methods for writing out
 * or reading in your data fields. Note that if you implement write object, you should also implement read object or there is
 * a good chance that the default reading behavior will not be able to successfully restore your object's fields.
 * <p>
 * Furthermore, while I have tried to test this these methods on a variety of classes, I cannot guarantee that it
 * will work in all cases. If bugs come up, you can try to fix them or let me know, and I will try to fix them. As
 * a further note, I am aware that this class does not contain elegant code. The read and write algorithms are complicated
 * and functionality is the priority.
 * <p>
 * This class relies on the SilentObjectCreator to get the SerializationConstructor that is used to construct
 * the read objects without invoking their constructors. This means that the SilentObjectCreator must use the sun.reflect
 * package and, therefore, could be broken in future Java versions. It also means that each object you wish to serialize
 * to XML using this class should implement the Serializable interface or inherit it from a superclass. Any objects it
 * contains and its superclasses should also implement Serializable whenever possible. Output may be unpredictable 
 * if this is not the case.
 * <p>
 * To allow subtypes of non-serializable classes to be serialized, the subtype may assume responsibility for saving and
 * restoring the state of the supertype's public, protected, and (if accessible) package fields. The subtype may assume
 * this responsibility only if the class it extends has an accessible no-arg constructor to initialize the class's state.
 * It is an error to declare a class Serializable in this case. The error will be detected at runtime. During deserialization,
 * the fields of non-serializable classes will be initialized using the public or protected no-arg constructor of the class.
 * A no-arg constructor must be accessible to the subclass that is serializable. The fields of serializable subclasses will
 * be restored from the stream
 * <p>
 * Finally, several objects are constructed using special cases, this is either to prevent security exceptions, as is
 * the case for the Class class, or to make their outputs less verbose, as is the case for Strings and Maps. I have
 * included test cases in {@link TestXMLObjectSerializer} that should help illustrate the usage of this class.
 * {@author Tristan Bepler}
 * <p>
 * The method formatXML has been added to create indentations for better readability, and also to omit the XML declaration
 * at the beginning of the file. {@author Alex Song}
 * <p>
 * Happy serializing.
 * 
 * @author Tristan Bepler
 * @author Alex Song
 * @version 1.1
 * 
 * @see Serializable
 * @see TestXMLObjectSerializer
 * @see XMLIgnore
 *
 */

public class XMLSerializerUtil {

	private static final String DOM_WILDCARD = "*";
	private static final String MODIFIERS = "modifiers";

	private static final String CUSTOM_READ = "readObjectXML";
	private static final String CUSTOM_WRITE = "writeObjectXML";

	private static final String VALUE = "value";
	private static final String KEY = "key";
	private static final String INDEX = "index";
	private static final String ENTRY = "entry";

	private static final String CLASSPATH = "classpath";
	private static final String SUPERCLASS = "superclass";
	private static final String CLASSFIELDS = "fields";

	private static final String REF_ID = "refId";
	private static final String SEE_REF_ID = "seeRefId";

	private static final Map<Class<?>, Class<?>> WRAPPER_TYPES = getWrapperTypes();

	private static final Map<Character, Character> REPLACEMENT_MAP = replacementMap();
	private static final Map<String, Class<?>> PRIMITIVE_CLASSES = primitiveClasses();

	public static boolean isWrapperType(Class<?> clazz){
		return WRAPPER_TYPES.values().contains(clazz);
	}

	public static Class<?> getWrapper(Class<?> clazz){
		return WRAPPER_TYPES.get(clazz);
	}

	private static Map<Character, Character> replacementMap(){
		Map<Character, Character> map = new HashMap<Character, Character>();
		map.put('$', '.');
		map.put('.', '$');
		return map;
	}

	private static Map<String, Class<?>> primitiveClasses(){
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("boolean", boolean.class);
		map.put("byte", byte.class);
		map.put("short", short.class);
		map.put("int", int.class);
		map.put("long", long.class);
		map.put("char", char.class);
		map.put("float", float.class);
		map.put("double", double.class);
		map.put("void", void.class);
		return map;
	}

	private static Map<Class<?>, Class<?>> getWrapperTypes(){
		Map<Class<?>, Class<?>> ret = new HashMap<Class<?>, Class<?>>();
		ret.put(Boolean.TYPE, Boolean.class);
		ret.put(Character.TYPE, Character.class);
		ret.put(Byte.TYPE, Byte.class);
		ret.put(Short.TYPE, Short.class);
		ret.put(Integer.TYPE, Integer.class);
		ret.put(Long.TYPE, Long.class);
		ret.put(Float.TYPE, Float.class);
		ret.put(Double.TYPE, Double.class);
		ret.put(Void.TYPE, Void.class);
		return ret;
	}

	/**
	 * This method parses the given value into the specified primitive or wrapper class.
	 * @param clazz - primitive or wrapper class used to parse
	 * @param value - string to be parsed
	 * @return object of type clazz parsed from the string
	 * @author Trisan Bepler
	 */
	public static Object toObject( Class<?> clazz, String value ) {
		if( Boolean.TYPE == clazz ) return Boolean.parseBoolean( value );
		if( Byte.TYPE == clazz ) return Byte.parseByte( value );
		if( Short.TYPE == clazz ) return Short.parseShort( value );
		if( Integer.TYPE == clazz ) return Integer.parseInt( value );
		if( Long.TYPE == clazz ) return Long.parseLong( value );
		if( Float.TYPE == clazz ) return Float.parseFloat( value );
		if( Double.TYPE == clazz ) return Double.parseDouble( value );
		if( Boolean.class == clazz ) return Boolean.parseBoolean( value );
		if( Byte.class == clazz ) return Byte.parseByte( value );
		if( Short.class == clazz ) return Short.parseShort( value );
		if( Integer.class == clazz ) return Integer.parseInt( value );
		if( Long.class == clazz ) return Long.parseLong( value );
		if( Float.class == clazz ) return Float.parseFloat( value );
		if( Double.class == clazz ) return Double.parseDouble( value );
		if( Character.class == clazz) return value.charAt(0);
		if( Character.TYPE == clazz) return value.charAt(0);
		return value;
	}
	
	/**
	 * This class is used to put fields on the element it is initialized with.
	 * 
	 * @author Tristan Bepler
	 *
	 */
	public static class WriteField{
		private Element cur;
		private Document doc;
		private List<Object> written;
		private WriteField(Element cur, Document doc, List<Object> written){
			this.cur = cur;
			this.doc = doc;
			this.written = written;
		}
		private void newChildText(String name, String content){
			Element e = doc.createElement(name);
			cur.appendChild(e);
			e.setTextContent(content);
		}
		public void write(String name, boolean value){
			newChildText(name, String.valueOf(value));
		}
		public void write(String name, char value){
			newChildText(name, String.valueOf(value));
		}
		public void write(String name, byte value){
			newChildText(name, String.valueOf(value));
		}
		public void write(String name, short value){
			newChildText(name, String.valueOf(value));
		}
		public void write(String name, int value){
			newChildText(name, String.valueOf(value));
		}
		public void write(String name, long value){
			newChildText(name, String.valueOf(value));
		}
		public void write(String name, float value){
			newChildText(name, String.valueOf(value));
		}
		public void write(String name, double value){
			newChildText(name, String.valueOf(value));
		}
		/**
		 * Creates a new child node with the given name and uses it to write the given object.
		 * @param name - name of new child node
		 * @param value - object to be written
		 * @throws ObjectWriteException
		 */
		public void write(String name, Object value) throws ObjectWriteException{
			Element child = doc.createElement(name);
			cur.appendChild(child);
			fillTreeRecurse(value, child, doc, written);
		}	
	}
	
	/**
	 * This class is used to read fields from the element it is initialized with.
	 * 
	 * @author Tristan Bepler
	 *
	 */
	public static class ReadField{
		private Element cur;
		private List<Element> children;
		private ClassLoader loader;
		private Map<Integer, Object> references;
		private ReadField(Element cur, ClassLoader loader, Map<Integer, Object> references){
			this.cur = cur;
			this.loader = loader;
			this.references = references;
			this.children = getDirectChildElementsByTag(cur, DOM_WILDCARD);
		}
		private Element getChild(String tag){
			for(Element e : children){
				if(e.getNodeName().equals(tag)){
					return e;
				}
			}
			return null;
		}
		/**
		 * Returns the class of this object
		 * @return class of object
		 * @throws ClassNotFoundException
		 */
		public Class<?> getObjectClass() throws ClassNotFoundException{
			if(cur.hasAttribute(CLASSPATH)){
				try{
					return loader.loadClass(cur.getAttribute(CLASSPATH));
				} catch(Exception e){
					return Class.forName(cur.getAttribute(CLASSPATH));
				}
			}
			return null;
		}
		public boolean read(String name, boolean def){
			Element e = getChild(name);
			if(e!=null){
				return Boolean.parseBoolean(e.getTextContent());
			}
			return def;
		}
		public char read(String name, char def){
			Element e = getChild(name);
			if(e!=null){
				return e.getTextContent().charAt(0);
			}
			return def;
		}
		public byte read(String name, byte def){
			Element e = getChild(name);
			if(e!=null){
				return Byte.parseByte(e.getTextContent());
			}
			return def;
		}
		public short read(String name, short def){
			Element e = getChild(name);
			if(e!=null){
				return Short.parseShort(e.getTextContent());
			}
			return def;
		}
		public int read(String name, int def){
			Element e = getChild(name);
			if(e!=null){
				return Integer.parseInt(e.getTextContent());
			}
			return def;
		}
		public long read(String name, long def){
			Element e = getChild(name);
			if(e!=null){
				return Long.parseLong(e.getTextContent());
			}
			return def;
		}
		public float read(String name, float def){
			Element e = getChild(name);
			if(e!=null){
				return Float.parseFloat(e.getTextContent());
			}
			return def;
		}
		public double read(String name, double def){
			Element e = getChild(name);
			if(e!=null){
				return Double.parseDouble(e.getTextContent());
			}
			return def;
		}
		/**
		 * Reads the specified node name for an object, returns the default object if no such node exists.
		 * @param name - name of child node to search
		 * @param def - default object to return
		 * @return - the object read from the child node with the given name or the default object if there
		 * was none
		 * @throws ObjectReadException
		 */
		public Object read(String name, Object def) throws ObjectReadException{
			Element e = getChild(name);
			if(e!=null){
				return readTreeRecurse(e, loader, references);
			}
			return def;
		}
	}
	
	/**
	 * This method is used to serialize the given object as XML to the given OutputStream. Objects should implement
	 * the Serializable interface or inherit it from a superclass. Furthermore, as many superclasses and contained
	 * classes should implement Serializable as possible. If they do not, behavior may be unpredictable. See this
	 * classes description for more information. {@link XMLSerializerUtil}
	 * @param o - object to be serialized to XML
	 * @param out - OutputStream this object will be written to
	 * @throws ObjectWriteException
	 * @author Tristan Bepler
	 */
	public static void serialize(Object o, OutputStream out) throws ObjectWriteException{
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			fillTreeRecurse(o, null, doc, new ArrayList<Object>());
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", 2);
			Transformer transformer = formatXML(transformerFactory.newTransformer());
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(out);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
		}catch(Exception e){
			throw new ObjectWriteException(e);
		}
	}


	/**
	 * This method is used to serialize the given object to XML. The object XML will be written to a file at the path
	 * specified by the given string. Objects should implement the Serializable interface or inherit it from a superclass.
	 * Furthermore, as many superclasses and contained classes should implement Serializable as possible. If they do not,
	 * behavior may be unpredictable. See this classes description for more information. {@link XMLSerializerUtil}
	 * @param o - object to be serialized to XML
	 * @param file - file the object XML should be written to
	 * @throws ObjectWriteException
	 * @author Tristan Bepler
	 */
	public static void write(Object o, String file) throws ObjectWriteException{
		try{
			serialize(o, new BufferedOutputStream(new FileOutputStream(file)));
		} catch (Exception e){
			throw new ObjectWriteException(e);
		}
	}

	/**
	 * This method recursively serializes objects.
	 * @author Tristan Bepler 
	 */
	private static void fillTreeRecurse(Object o, Element parent, Document doc, List<Object> written) throws ObjectWriteException{
		try{
			// write nothing if o is null
			if(o == null){
				return;
			}
			//initialize parent node if uninitialized
			if(parent == null){
				parent = doc.createElement(o.getClass().getSimpleName());
				doc.appendChild(parent);
			}
			//set the classpath of this object as an attribute of the node
			parent.setAttribute(CLASSPATH, o.getClass().getName());
			//simply write the objects value if it is a primitive wrapper
			if(XMLSerializerUtil.isWrapperType(o.getClass())){
				parent.setTextContent(String.valueOf(o));
				return;
			}
			//if the object references an object that has already been written, tag it's refId
			if(written.contains(o)){
				parent.setAttribute(SEE_REF_ID, String.valueOf(written.indexOf(o)));
				return;
			}
			//add this object to the written object array and set its refId
			written.add(o);
			parent.setAttribute(REF_ID, String.valueOf(written.indexOf(o)));
			//special cases
			if(writeSpecialCaseObjectIsString(o, parent, doc, written)){
				return;
			}
			if(writeSpecialCaseObjectIsClass(o, parent, doc, written)){
				return;
			}
			if(writeSpecialCaseObjectIsMap(o, parent, doc, written)){
				return;
			}
			//if object is an array, have to handle it specially, recursively write array elements
			if(o.getClass().isArray()){
				writeArray(o, parent, doc, written);
				return;
			}
			//object is a proper object, check and write its superclass and fill all fields
			writeObject(o, parent, doc, written);
		} catch (IllegalArgumentException e){
			throw new ObjectWriteException(e);
		} catch (IllegalAccessException e){
			throw new ObjectWriteException();
		}
	}
	
	/**
	 * Writes the object by recursively iterating over its class and superclass fields
	 * 
	 * @author Tristan Bepler
	 */
	private static void writeObject(Object o, Element parent, Document doc, List<Object> written) throws IllegalAccessException, ObjectWriteException {
		//init class
		Class<?> clazz = o.getClass();
		//now write the class and superclass fields recursively
		writeFieldsRecurse(o, parent, doc, written, clazz);
	}
	
	/**
	 * Recursively writes the fields of this class and its superclasses
	 * 
	 * @author Tristan Bepler
	 */
	private static void writeFieldsRecurse(Object o, Element parent, Document doc, List<Object> written, Class<?> clazz) throws IllegalAccessException, ObjectWriteException {
		if(!Serializable.class.isAssignableFrom(clazz)){
			try {
				Constructor con = clazz.getDeclaredConstructor();
				if(con.getModifiers() != Modifier.PUBLIC && con.getModifiers() != Modifier.PROTECTED){
					throw new ObjectWriteException("Non-serializable class \""+clazz+"\" does not have a public or protected default constructor.");
				}
			} catch (Exception e){
				throw new ObjectWriteException("Non-serializable class \""+clazz+"\" does not have a public or protected default constructor.");
			}
			return;
		}
		Element classNode = doc.createElement(CLASSFIELDS);
		parent.appendChild(classNode);
		WriteField writer = new WriteField(classNode, doc, written);
		//check if custom write method defined
		try {
			Method write = clazz.getDeclaredMethod(CUSTOM_WRITE, WriteField.class);
			if(write.getExceptionTypes()[0] == ObjectWriteException.class){
				write.setAccessible(true);
				write.invoke(o, writer);
			}
		}catch (InvocationTargetException e){
			throw new ObjectWriteException(e);
		} catch (Exception e){
			writeFieldDefault(o, clazz, writer);
		}
		if(!classNode.hasChildNodes()){
			parent.removeChild(classNode);
		}
		clazz = clazz.getSuperclass();
		if(clazz == null){
			return;
		}
		Element superNode = doc.createElement(SUPERCLASS);
		parent.appendChild(superNode);
		superNode.setAttribute(CLASSPATH, clazz.getName());
		writeFieldsRecurse(o, superNode, doc, written, clazz);
	}
	
	/**
	 * The default field writing method
	 * 
	 * @author Tristan Bepler
	 */
	private static void writeFieldDefault(Object o, Class<?> clazz, WriteField writer) throws IllegalAccessException, ObjectWriteException {
		for(Field f : clazz.getDeclaredFields()){
			f.setAccessible(true);
			Object value = f.get(o);
			//ignore this field if it is null or static and final or annotated by @XMLIgnore
			if(value==null){
				continue;
			}
			if(f.isAnnotationPresent(XMLIgnore.class)){
				continue;
			}
			if(Modifier.isStatic(f.getModifiers())&&Modifier.isFinal(f.getModifiers())){
				continue;
			}
			String name = f.getName();
			name = name.replace('$', REPLACEMENT_MAP.get('$'));
			writer.write(name, value);
		}
	}
	
	/**
	 * Writes array by iterating over the elements
	 * 
	 * @author Tristan Bepler
	 */
	private static void writeArray(Object o, Element parent, Document doc, List<Object> written) throws IllegalAccessException, ArrayIndexOutOfBoundsException, IllegalArgumentException, ObjectWriteException {
		for(int i=0; i<Array.getLength(o); i++){
			Element entry = doc.createElement(ENTRY);
			parent.appendChild(entry);
			entry.setAttribute(INDEX, String.valueOf(i));
			fillTreeRecurse(Array.get(o, i), entry, doc, written);
		}
	}

	/**
	 * Returns all the unique fields of the classes in the given list.
	 * @author Tristan Bepler
	 */
	private static Field[] getAllFields(List<Class<?>> classes){
		Set<Field> fields = new HashSet<Field>();
		for(Class<?> clazz : classes){
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		}
		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 * Method for handling serialization of String objects.
	 * @author Tristan Bepler
	 */
	private static boolean writeSpecialCaseObjectIsString(Object o, Element cur, Document doc, List<Object> written){
		if(o instanceof String){
			cur.setTextContent((String) o);
			return true;
		}
		return false;
	}

	/**
	 * Method for handling deserialization of String objects.
	 * @author Tristan Bepler
	 */
	private static String readSpecialCaseObjectIsString(Class<?> clazz, Element cur){
		if(clazz == String.class){
			return cur.getTextContent();
		}
		return null;
	}

	/**
	 * Method for handling serialization of class objects.
	 * @author Tristan Bepler
	 */
	private static boolean writeSpecialCaseObjectIsClass(Object o, Element cur, Document doc, List<Object> written){
		if(o instanceof Class){
			cur.setTextContent(((Class)o).getName());
			return true;
		}
		return false;
	}

	/**
	 * Method for handling deserialization of class objects.
	 * @author Tristan Bepler
	 */
	private static Object readSpecialCaseObjectIsClass(Class<?> clazz, Element cur, ClassLoader loader) throws ClassNotFoundException{
		if(Class.class == clazz){
			if(PRIMITIVE_CLASSES.containsKey(cur.getTextContent())){
				return PRIMITIVE_CLASSES.get(cur.getTextContent());
			}
			try{
				return loader.loadClass(cur.getTextContent());
			} catch (Exception e){
				return Class.forName(cur.getTextContent());
			}
		}
		return null;
	}

	/**
	 * Method for handling serialization of map objects. Maps were very verbose before.
	 * @author Tristan Bepler 
	 */
	private static boolean writeSpecialCaseObjectIsMap(Object o, Element cur, Document doc, List<Object> written) throws IllegalArgumentException, IllegalAccessException, ObjectWriteException {
		if(o instanceof Map){
			Map map = (Map) o;
			for(Object key : map.keySet()){
				Element entry = doc.createElement(ENTRY);
				cur.appendChild(entry);
				WriteField writer = new WriteField(entry, doc, written);
				writer.write(KEY, key);
				writer.write(VALUE, map.get(key));
			}
			return true;
		}
		return false;
	}

	/**
	 * This method handled deserialization of map objects.
	 * @author Tristan Bepler
	 */
	private static Map readSpecialCaseObjectIsMap(Class<?> clazz, Element cur, ClassLoader loader, Map<Integer, Object> references) throws Exception{
		if(Map.class.isAssignableFrom(clazz)){
			try{
				Constructor con = clazz.getConstructor();
				con.setAccessible(true);
				Map map = (Map) con.newInstance();
				addReference(cur, references, map);
				List<Element> entries = getDirectChildElementsByTag(cur, ENTRY);
				for(Element entry: entries){
					ReadField reader = new ReadField(entry, loader, references);
					Object key = reader.read(KEY, null);
					Object value = reader.read(VALUE, null);
					map.put(key, value);
				}
				return map;
			}catch (Exception e){
				throw new Exception(e);
			}
		}
		return null;
	}
	
	/**
	 * This method is used to deserialize a previously XML serialized object from the given InputStream. The ClassLoader
	 * will be used to retrieve the classes required for object instantiation. This is to allow loading of classes
	 * from external sources if necessary. See this classes description for more information on object serialization
	 * and deserialization.{@link XMLSerializerUtil}
	 * @param in - the InputStream the object will be deserialized from
	 * @param loader - a ClassLoader that will be used to load Class objects
	 * @return - the deserialized object
	 * @throws ObjectReadException
	 * @author Tristan Bepler
	 */
	public static Object deserialize(InputStream in, ClassLoader loader) throws ObjectReadException{
		try {
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(in));
			Document doc = parser.getDocument();
			doc.getDocumentElement().normalize();
			return readTreeRecurse(doc.getDocumentElement(), loader, new HashMap<Integer, Object>());
		} catch (Exception e) {
			throw new ObjectReadException(e);
		}
	}
	
	/**
	 * This method is used to deserialize a previously XML serialized object from the given InputStream. See this
	 * classes description for more information on object serialization and deserialization.{@link XMLSerializerUtil}
	 * @param in - the InputStream the object will be deserialized from
	 * @return - the deserialized object
	 * @throws ObjectReadException
	 * @author Trisan Bepler
	 */
	public static Object deserialize(InputStream in) throws ObjectReadException{
		return deserialize(in, ClassLoader.getSystemClassLoader());
	}

	/**
	 * This method is used to deserialize a previously serialized object from the XML file it was written to. The
	 * ClassLoader will be used to retrieve the classes required for object instantiation. This is to allow loading
	 * of classes from external sources if necessary. See this classes description for more information on object
	 * serialization and deserialization.{@link XMLSerializerUtil}
	 * @param file - the file containing the object XML
	 * @param loader - a ClassLoader that will be used to load Class objects.
	 * @return - the deserialized object
	 * @throws ObjectReadException
	 * @author Tristan Bepler
	 */
	public static Object read(String file, ClassLoader loader) throws ObjectReadException{
		try {
			return deserialize(new BufferedInputStream(new FileInputStream(file)), loader);
		} catch (Exception e) {
			throw new ObjectReadException(e);
		}

	}

	/**
	 * This method is used to deserialize and object from its serialized object XML. The path of the file the object
	 * should be deserialized from is specified by the file parameter. See this classes description for more
	 * information about object serialization and deserialization.{@link XMLSerializerUtil}
	 * @param file - the file containing the object XML
	 * @return - the deserialized object
	 * @throws ObjectReadException
	 * @author Tristan Bepler
	 */
	public static Object read(String file) throws ObjectReadException{
		return read(file, ClassLoader.getSystemClassLoader());
	}

	/**
	 * This method recursively deserializes objects
	 * @author Tristan Bepler
	 */
	private static Object readTreeRecurse(Element cur, ClassLoader loader, Map<Integer, Object> references) throws ObjectReadException{
		try{
			//if cur is null return null
			if(cur == null){
				return null;
			}
			//if cur is already written elswhere, read it from it's reference
			if(cur.hasAttribute(SEE_REF_ID)){
				int refId = Integer.parseInt(cur.getAttribute(SEE_REF_ID));
				try{
					return references.get(refId);
				} catch(Exception e){
					throw new ObjectReadException("Error: could find reference object");
				}
			}
			//if cur has no classpath specified, return null
			if(!cur.hasAttribute(CLASSPATH)){
				return null;
			}
			//get the class of cur
			Class<?> c;
			try{
				c = (Class<?>) loader.loadClass(cur.getAttribute(CLASSPATH));
			} catch(Exception e){
				c = (Class<?>) Class.forName(cur.getAttribute(CLASSPATH));
			}
			//if cur is a wrapper type, parse and return it
			if(isWrapperType(c)){
				return toObject(c, cur.getTextContent().trim());
			}
			//if cur is an instance of the Class class
			Object isClass = readSpecialCaseObjectIsClass(c, cur, loader);
			if(isClass != null){
				addReference(cur, references, isClass);
				return isClass;
			}			
			//special case, if cur is a string return it
			Object isString = readSpecialCaseObjectIsString(c, cur);
			if(isString != null){
				addReference(cur, references, isString);
				return isString;
			}
			//special case, if cur is a map return it
			Object isMap = readSpecialCaseObjectIsMap(c, cur, loader, references);
			if(isMap != null){
				return isMap;
			}
			//if cur is an array, instantiate it and recursively build its elements
			if(c.isArray()){
				return readArray(c, cur, loader, references);
			}
			//get first non-serializable superclass
			Class nonSerial = c;
			while(Serializable.class.isAssignableFrom(nonSerial)){
				nonSerial = nonSerial.getSuperclass();
			}
			//create object using constructor of its highest level superclass not implementing serializable
			Object readObject = SilentObjectCreator.create(c, nonSerial);
			//add object to the references map
			addReference(cur, references, readObject);
			readFieldsRecurse(readObject, cur, loader, references, c);
			return readObject;

		} catch (Exception e) {
			throw new ObjectReadException(e);
		}


	}
	
	
	/**
	 * Fills this class and super classes' fields recursively.
	 * 
	 * @author Tristan Bepler
	 */
	private static void readFieldsRecurse(Object readObject, Element cur, ClassLoader loader, Map<Integer, Object> references, Class<?> clazz) throws ObjectReadException, NoSuchFieldException, IllegalAccessException {
		if(!Serializable.class.isAssignableFrom(clazz)){
			return;
		}
		if(hasChild(cur, CLASSFIELDS)){
			Element classNode = getDirectChildElementsByTag(cur, CLASSFIELDS).get(0);
			//check if custom read method defined
			try {
				Method read = clazz.getDeclaredMethod(CUSTOM_READ, ReadField.class);
				if(read.getExceptionTypes()[0] == ObjectReadException.class){
					ReadField reader = new ReadField(classNode, loader, references);
					read.setAccessible(true);
					read.invoke(readObject, reader);
				}
			} catch (InvocationTargetException e){
				throw new ObjectReadException(e);
			} catch (Exception e){
				//try default
				readFieldsDefault(classNode, loader, references, readObject, clazz);
			}
		}
		//fill superclass fields recursively
		clazz = clazz.getSuperclass();
		if(clazz == null){
			return;
		}
		Element superNode = getDirectChildElementsByTag(cur, SUPERCLASS).get(0);
		readFieldsRecurse(readObject, superNode, loader, references, clazz);
	}
	
	/**
	 * Iterates over and fills all the object's fields
	 * 
	 * @author Tristan Bepler
	 */
	private static void readFieldsDefault(Element cur, ClassLoader loader, Map<Integer, Object> references, Object readObject, Class<?> clazz) throws NoSuchFieldException, IllegalAccessException, ObjectReadException {
		for(Field f : clazz.getDeclaredFields()){
			f.setAccessible(true);
			//if field is final static or transient, ignore
			if(Modifier.isStatic(f.getModifiers())&&Modifier.isFinal(f.getModifiers())){
				continue;
			}
			//if field is annoted with @XMLIgnore, ignore
			if(f.isAnnotationPresent(XMLIgnore.class)){
				continue;
			}
			//if field is final, remove final modifier
			if(Modifier.isFinal(f.getModifiers())){
				Field mods = Field.class.getDeclaredField(MODIFIERS);
				mods.setAccessible(true);
				mods.setInt(f, f.getModifiers() & ~Modifier.FINAL);
			}
			String name = f.getName();
			name = name.replace('$', REPLACEMENT_MAP.get('$'));
			ReadField reader = new ReadField(cur, loader, references);
			f.set(readObject, reader.read(name, null));
		}
	}
	
	/**
	 * Returns a list of classes containing the class of the object and all its super classes
	 * 
	 * @author Tristan Bepler
	 */
	private static List<Class<?>> getSupers(Object o) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		Class<?> curClass = o.getClass();
		while(curClass != null){
			classes.add(curClass);
			curClass = curClass.getSuperclass();
		}
		return classes;
	}
	
	/**
	 * Builds the array by iterating over its elements
	 * 
	 * @author Tristan Bepler
	 */
	private static Object readArray(Class<?> c, Element cur, ClassLoader loader, Map<Integer, Object> references) throws ObjectReadException {
		List<Element> children = getDirectChildElementsByTag(cur, ENTRY);
		Object readArray = Array.newInstance(c.getComponentType(), children.size());
		addReference(cur, references, readArray);
		for(Element entry : children){
			int index = Integer.parseInt(entry.getAttribute(INDEX));
			Array.set(readArray, index, readTreeRecurse(entry, loader, references));
		}
		return readArray;
	}
	
	
	/**
	 * Adds a reference for the given object to the references map if it has a refId
	 *
	 * @author Tristan Bepler
	 */
	private static void addReference(Element cur, Map<Integer, Object> references, Object o) {
		if(cur.hasAttribute(REF_ID)){
			int id = Integer.parseInt(cur.getAttribute(REF_ID));
			references.put(id, o);
		}
	}

	/**
	 * This method returns a list of the direct element node children of this element node with the specified tag.
	 * @param node - parent node
	 * @param tag - tag of direct children to be returned
	 * @return a list containing the direct element children with the given tag
	 * @author Tristan Bepler
	 */
	public static List<Element> getDirectChildElementsByTag(Element node, String tag){
		List<Element> children = new ArrayList<Element>();
		Node child = node.getFirstChild();
		while(child!=null){
			if(child.getNodeType() == Node.ELEMENT_NODE && (child.getNodeName().equals(tag) || tag.equals(DOM_WILDCARD))){
				children.add((Element) child);
			}
			child = child.getNextSibling();
		}
		return children;
	}
	
	/**
	 * This method checks whether the given element node has a child element with the given name.
	 * @param node - parent to check
	 * @param name - name of child
	 * @return - true if parent has a child with the given name, false otherwise
	 */
	public static boolean hasChild(Element node, String name){
		List<Element> children = getDirectChildElementsByTag(node, DOM_WILDCARD);
		for(Element e : children){
			if(e.getNodeName().equals(name)){
				return true;
			}
		}
		return false;
	}

	/**
	 * This method formats the XML file by omitting the XML Declaration and 
	 * creating indentations 
	 * @param transformer - transformer that is used to process XML
	 * @return a transformer that omits the XML declaration and performs indentations  
	 * @author Alex Song
	 */
	private static Transformer formatXML(Transformer transformer) {
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		return transformer;
	}






}
