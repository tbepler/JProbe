package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class ClassLoaderObjectInputStream extends ObjectInputStream{

	private ClassLoader m_Loader;
	
	public ClassLoaderObjectInputStream(InputStream in, ClassLoader loader) throws IOException {
		super(in);
		m_Loader = loader;
	}
	
	public void setClassLoader(ClassLoader loader){
		m_Loader = loader;
	}
	
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException{
		try{
			return m_Loader.loadClass(desc.getName());
		} catch (Exception e){
			return super.resolveClass(desc);
		}
	}
	
}
