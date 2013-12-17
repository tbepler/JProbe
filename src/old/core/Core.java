package old.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import old.controller.CoreController;
import old.core.exceptions.CoreInitializationException;
import old.core.exceptions.DataReadException;
import old.core.exceptions.DataWriteException;
import old.core.exceptions.FileReadException;
import old.core.exceptions.FormatNotSupportedException;
import old.core.exceptions.ModuleExecutionException;
import old.core.exceptions.NoSuchModuleException;
import old.datatypes.DataType;
import old.modules.*;
import old.readwrite.DataReader;
import old.readwrite.DataWriter;

public class Core extends Observable implements Serializable{
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
	
	public boolean isWritable(DataType data){
		if(data == null) return false;
		String id = dataRegistry.getId(data.getClass());
		return dataRegistry.getWriterIds().contains(id);
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
			this.setChanged();
			this.notifyObservers(new DataEvent(data, EVENT_DATA_REMOVED));
		}
	}
	
	public void removeData(int index){
		DataType removed = data.remove(index);
		if(removed != null){
			controller.update(EVENT_DATA_REMOVED);
			this.setChanged();
			this.notifyObservers(new DataEvent(removed, EVENT_DATA_REMOVED));
		}
	}
	
	public void clearData(){
		data.clear();
		controller.update(EVENT_DATA_REMOVED);
		this.setChanged();
		this.notifyObservers(new DataEvent(null, DataEvent.EVENT_DATA_CLEARED));
	}
	
	public void addData(DataType data){
		this.data.add(data);
		controller.update(EVENT_DATA_ADDED);
		this.setChanged();
		this.notifyObservers(new DataEvent(data, DataEvent.EVENT_DATA_ADDED));
	}
	
	public Map<String, String[]> getWritableFormats(DataType data){
		return getWritableFormats(dataRegistry.getId(data.getClass()));
	}
	
	public Map<String, String[]> getWritableFormats(String dataType){
		return dataRegistry.getWriter(dataType).getValidWriteFormats();
	}
	
	public void write(DataType data, File file, String fileFormat) throws DataWriteException{
		String id = dataRegistry.getId(data.getClass());
		if(id == null){
			throw new DataWriteException(data.getClass()+" not writable");
		}
		DataWriter writer = dataRegistry.getWriter(id);
		if(writer == null){
			throw new DataWriteException(data.getClass()+" not writable");
		}
		try {
			writer.write(data, fileFormat, new BufferedWriter(new FileWriter(file)));
		} catch (Exception e){
			throw new DataWriteException(e);
		}
		
	}
	
	public Map<String, String[]> getReadableFormats(String dataType){
		return dataRegistry.getReader(dataType).getValidReadFormats();
	}
	
	public void read(String dataType, File file, String fileFormat) throws DataReadException{
		DataReader reader = dataRegistry.getReader(dataType);
		if(reader == null){
			throw new DataReadException(dataType+" not readable");
		}
		try {
			DataType read = reader.read(fileFormat, new Scanner(file));
			this.addData(read);
		} catch (Exception e){
			throw new DataReadException(e);
		}
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
