package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;

public class OSGIUtils {
	
	public static Bundle getProvider(Class<?> clazz, BundleContext context){
		Bundle[] installed = context.getBundles();
		ClassLoader loader = clazz.getClassLoader();
		for(Bundle bundle : installed){
			if(getBundleClassLoader(bundle) == loader){
				return bundle;
			}
		}
		return null;
	}
	
	public static Bundle getBundleWithName(String symbolicName, BundleContext context){
		for(Bundle bundle : context.getBundles()){
			if(bundle.getSymbolicName().equals(symbolicName)){
				return bundle;
			}
		}
		return null;
	}
	
	public static ClassLoader getBundleClassLoader(Bundle bundle){
		return bundle.adapt(BundleWiring.class).getClassLoader();
	}
	
	public static ObjectInputStream newBundleObjectInputStream(InputStream in, Bundle bundle) throws IOException{
		return new ClassLoaderObjectInputStream(in, getBundleClassLoader(bundle));
	}
	
}
