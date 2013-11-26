package core;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import controller.CoreController;
import datatypes.DataType;
import exceptions.CoreInitializationException;
import exceptions.ModuleExecutionException;
import exceptions.NoSuchModuleException;
import modules.*;

public class Core implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final int EVENT_DATA_ADDED = 1;
	public static final int EVENT_DATA_REMOVED = 2;
	
	private CoreController controller;
	private ModuleRegistry moduleRegistry;
	private DataRegistry dataRegistry;
	private List<DataType> data;

	public Core(CoreController controller, String extensionsDirectory, String modulesXML, String datatypesXML) throws CoreInitializationException{
		try{
			moduleRegistry = new ModuleRegistry(extensionsDirectory, modulesXML);
			dataRegistry = new DataRegistry(extensionsDirectory, datatypesXML);
		}catch(Exception e){
			throw new CoreInitializationException(e);
		}
		this.controller = controller;
		this.data = new ArrayList<DataType>();
	}
	
	public void setController(CoreController o){
		this.controller = o;
	}

	public Collection<String> getModuleNames(){
		return moduleRegistry.getModuleNames();
	}
	
	public String getModuleDescription(String name){
		return moduleRegistry.getDescription(name);
	}
	
	public Collection<String> getDataTypes(){
		return dataRegistry.getDataTypeIds();
	}
	
	public Collection<String> getReadableDataTypes(){
		return dataRegistry.getReaderIds();
	}
	
	public Collection<String> getWritableDataTypes(){
		return dataRegistry.getWriterIds();
	}
	
	public List<DataType> getAllData(){
		return Collections.unmodifiableList(data);
	}
	
	public DataType getDate(int index){
		return data.get(index);
	}
	
	public void removeData(DataType data){
		if(this.data.remove(data)){
			controller.update(EVENT_DATA_REMOVED);
		}
	}
	
	public void removeData(int index){
		data.remove(index);
		controller.update(EVENT_DATA_REMOVED);
	}
	
	public void clearData(){
		data.clear();
		controller.update(EVENT_DATA_REMOVED);
	}
	
	public Map<String, String[]> getWritableFormats(String dataType){
		return dataRegistry.getWriter(dataType).getValidWriteFormats();
	}
	
	public void write(DataType data, File file, String fileFormat){
		
	}
	
	public Map<String, String[]> getReadableFormats(String dataType){
		return dataRegistry.getReader(dataType).getValidReadFormats();
	}
	
	public void read(String dataType, File file, String fileFormat){
		
	}
	
	public void runModule(String name) throws NoSuchModuleException, ModuleExecutionException{
		Module mod = moduleRegistry.getModule(name);
		if(mod == null){
			throw new NoSuchModuleException("Could not find module \""+name+"\"");
		}
		DataType[] args = controller.selectArgs(mod.getRequiredArgs(), mod.getOptionalArgs());
		if(args == null){
			return;
		}
		try{
			DataType[] required = new DataType[mod.getRequiredArgs().length];
			DataType[] optional = new DataType[mod.getOptionalArgs().length];
			System.arraycopy(args, 0, required, 0, required.length);
			System.arraycopy(args, required.length, optional, 0, optional.length);
			DataType results = mod.run(required, optional);
			data.add(results);
			controller.update(EVENT_DATA_ADDED);
		}catch(Exception e){
			throw new ModuleExecutionException(e);
		}
	}

	
}
