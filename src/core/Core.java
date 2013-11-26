package core;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import controller.CoreController;
import datatypes.DataType;
import exceptions.CoreInitializationException;
import exceptions.IllegalModuleException;
import exceptions.NoSuchModuleException;
import modules.*;

public class Core implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private CoreController controller;
	private ModuleRegistry modules;
	private DataRegistry data;

	public Core() throws CoreInitializationException{
		try{
			modules = new ModuleRegistry("Extensions", "src/modules.xml");
			data = new DataRegistry("Extensions", "src/datatypes.xml");
		}catch(Exception e){
			throw new CoreInitializationException(e);
		}
		controller = null;
	}
	
	public void addObserver(CoreController o){
		this.controller = o;
	}
	
	public void removeObserver(CoreController o){
		this.controller = null;
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
		DataType[] args = null;
	}

	
}
