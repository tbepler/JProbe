package old.core;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import old.modules.*;

import org.xml.sax.SAXException;

import util.ExtensionUtils;
import util.XMLParser;

public class ModuleRegistry {
	
	private Map<String, String> descriptions;
	private Map<String, Module> modules;
	
	public ModuleRegistry(String directory, String registry) throws URISyntaxException, IOException, SAXException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		
		descriptions = new TreeMap<String, String>();
		modules = new TreeMap<String, Module>();
		
		ClassLoader loader = ExtensionUtils.createDirectoryLoader(directory);
		XMLParser parser = new XMLParser(registry);
		
		List<String[]> entries = parser.parseModules();
		for(String[] entry : entries){
			descriptions.put(entry[0], entry[2]);
			Class<? extends Module>	clazz = (Class<? extends Module>) loader.loadClass(entry[1]);
			modules.put(entry[0], clazz.newInstance());
		}
	}
	
	public Collection<String> getModuleNames(){
		return new ArrayList<String>(modules.keySet());
	}
	
	public String getDescription(String name){
		return descriptions.get(name);
	}
	
	public Module getModule(String name){
		return modules.get(name);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
