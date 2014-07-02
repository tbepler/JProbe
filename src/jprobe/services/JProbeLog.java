package jprobe.services;

import org.osgi.framework.Bundle;

import util.logging.Log;
import util.logging.Log.Level;

public class JProbeLog {
	
	private static JProbeLog m_Instance = new JProbeLog();
	
	private Log m_Log = null;
	
	private JProbeLog(){
		//block instantiation
	}
	
	public static JProbeLog getInstance(){
		return m_Instance;
	}
	
	public void init(Log journal){
		//only init if log is still null
		if(m_Log == null){
			m_Log = journal;
		}
	}
	
	public synchronized void write(Level level, Bundle writer, String message){
		if(Debug.getLevel() == Debug.OFF && level == Level.DEBUG){
			return;
		}
		if(m_Log != null){
			StringBuilder builder = new StringBuilder();
			builder.append("(");
			if(writer != null){
				builder.append(writer.getSymbolicName());
			}else{
				builder.append("UnknownBundle");
			}
			builder.append(") ");
			builder.append(message);
			m_Log.write(level, builder.toString());
		}
	}
	
}
