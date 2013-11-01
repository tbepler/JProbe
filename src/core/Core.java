package core;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import exceptions.IllegalModuleException;

import modules.*;

public class Core {
	
	private Map<String, Module> moduleObjectMap;
	private Map<String, String> moduleDescriptionMap;
	
	public Core(String moduleXML) throws ParserConfigurationException, SAXException, IOException{
		registerModules(moduleXML);
	}
	
	private void registerModules(String moduleXML) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse (new File("book.xml"));
		// normalize text representation
		doc.getDocumentElement ().normalize ();
		NodeList moduleList = doc.getElementsByTagName("module");
		for(int i=0; i<moduleList.getLength(); i++){
			Element module = (Element) moduleList.item(i);
			String name = module.getElementsByTagName("name").item(0).getTextContent();
			String classpath = module.getElementsByTagName("classpath").item(0).getTextContent();
			String description = module.getElementsByTagName("description").item(0).getTextContent();
			try{
				moduleObjectMap.put(name, instantiateModule(classpath));
				moduleDescriptionMap.put(name, description);
			} catch (IllegalModuleException e){
				System.err.println(e.getMessage());
			} catch (ClassNotFoundException e){
				System.err.println(e.getMessage());
			}
		}
	}
	
	private Module instantiateModule(String classpath) throws ClassNotFoundException, IllegalModuleException{
		Class clazz = Class.forName(classpath);
		try {
			Object o = clazz.newInstance();
			if(o instanceof Module){
				return (Module) o;
			}
			throw new IllegalModuleException("Error: \""+classpath+"\" is not a module object.");
		} catch (Exception e) {
			throw new IllegalModuleException(e);
		}
	}
}
