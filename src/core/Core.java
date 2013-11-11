package core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import exceptions.CoreInitializationException;
import exceptions.IllegalModuleException;
import exceptions.NoSuchModuleException;

import modules.*;

public class Core extends Observable{
	
	private ModuleRegistry modules;
	private DataRegistry data;

	public Core() throws CoreInitializationException{
		try{
			modules = new ModuleRegistry("Extensions", "src/modules.xml");
			data = new DataRegistry("Extensions", "src/datatypes.xml");
		}catch(Exception e){
			throw new CoreInitializationException(e);
		}
	}

	public Collection<String> getModuleNames(){
		return modules.getModuleNames();
	}
	
	public String getModuleDescription(String name){
		return modules.getDescription(name);
	}
	
	public void runModule(String name) throws NoSuchModuleException{
		Module mod = modules.getModule(name);
		if(mod == null){
			throw new NoSuchModuleException("Could not find module \""+name+"\"");
		}
		
	}

	
}
