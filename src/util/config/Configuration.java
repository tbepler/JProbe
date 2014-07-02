package util.config;

import java.util.HashMap;
import java.util.Map;

public abstract class Configuration {
	
	private final Map<String, String> m_Settings = new HashMap<String, String>();
	
	public void putSetting(String key, String value){
		m_Settings.put(key, value);
	}
	
	public void removeSetting(String key){
		m_Settings.remove(key);
	}
	
	public String getSetting(String key){
		return m_Settings.get(key);
	}
	
	public boolean containsSetting(String key){
		return m_Settings.containsKey(key);
	}
	
}
