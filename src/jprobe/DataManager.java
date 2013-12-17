package jprobe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jprobe.services.Data;
import jprobe.services.DataReader;
import jprobe.services.DataWriter;

public class DataManager {
	
	private Map<Class<? extends Data>, List<Data>> data;
	private Map<String, Data> nameToData;
	private Map<Data, String> dataToName;
	private Map<Class<? extends Data>, Integer> counts;
	private Map<Class<? extends Data>, DataReader> readers;
	private Map<Class<? extends Data>, DataWriter> writers;
	
	public DataManager(){
		data = new HashMap<Class<? extends Data>, List<Data>>();
		nameToData = new HashMap<String, Data>();
		dataToName = new HashMap<Data, String>();
		counts = new HashMap<Class<? extends Data>, Integer>();
		readers = new HashMap<Class<? extends Data>, DataReader>();
		writers = new HashMap<Class<? extends Data>, DataWriter>();
	}
	
	private String assignName(Data d){
		int count;
		if(counts.containsKey(d)){
			count = counts.get(d.getClass()) + 1;
		}else{
			count = 1;
		}
		String name = d.getClass().getSimpleName()+String.valueOf(count);
		while(nameToData.containsKey(name)){
			name = d.getClass().getSimpleName()+String.valueOf(++count);
		}
		return name;
	}
	
	public void addData(Data d, String name){
		Class<? extends Data> clazz = d.getClass();
		if(!data.containsKey(clazz)){
			List<Data> list = new ArrayList<Data>();
			list.add(d);
			data.put(clazz, list);
			counts.put(clazz, 1);
		}else{
			data.get(clazz).add(d);
			counts.put(clazz, counts.get(clazz)+1);
		}
		nameToData.put(name, d);
		dataToName.put(d, name);
	}
	
	public void addData(Data d){
		addData(d, assignName(d));
	}
	
	private void removeData(String name, Data d){
		data.get(d.getClass()).remove(d);
		nameToData.remove(name);
		dataToName.remove(d);
	}
	
	public void removeData(String name){
		removeData(name, nameToData.get(name));
	}
	
	public void removeData(Data d){
		removeData(dataToName.get(d), d);
	}
	
	public List<Data> getAllData(){
		List<Data> full = new ArrayList<Data>();
		for(List<Data> part : data.values()){
			full.addAll(part);
		}
		return full;
	}
	
	public List<Data> getData(Class<? extends Data> type){
		return Collections.unmodifiableList(data.get(type));
	}
	
	public Data getData(String name){
		return nameToData.get(name);
	}
	
	public String getDataName(Data d){
		return dataToName.get(d);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
