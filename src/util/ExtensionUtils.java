package util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

/**
 * This class contains methods that may be useful for creating extensibility.
 * @author Tristan Bepler
 *
 */

public class ExtensionUtils {
	
	/**
	 * This method returns a new ClassLoader object that can be used to load classes from files contained by the specified
	 * directory.
	 * @param directory - the path of the directory the ClassLoader should load from
	 * @return a ClassLoader that can be used to load classes and resources from files in the specified directory
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws IOException 
	 * @see ClassLoader
	 */
	public static ClassLoader createDirectoryLoader(String directory) throws URISyntaxException, IOException{
		Collection<URL> urls = new ArrayList<URL>();
		File dir = new File(directory);
		File[] files = dir.listFiles();
		for(File f : files){
			System.out.println(f.getCanonicalPath());
			urls.add(f.toURI().toURL());
		}

		return URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]));
	}

	
	
}
