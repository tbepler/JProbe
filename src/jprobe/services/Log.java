package jprobe.services;

import org.osgi.framework.Bundle;

public class Log {
	
	private static Log m_Instance = new Log();
	
	private Journal m_Journal = null;
	
	private Log(){
		//block instantiation
	}
	
	public static Log getInstance(){
		return m_Instance;
	}
	
	public void init(Journal journal){
		//only init if journal is still null
		if(m_Journal == null){
			m_Journal = journal;
		}
	}
	
	public synchronized void write(Bundle writer, String message){
		if(m_Journal != null){
			m_Journal.write(writer, message);
		}
	}
	
}
