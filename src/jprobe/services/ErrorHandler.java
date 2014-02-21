package jprobe.services;

import java.util.Collection;
import java.util.HashSet;

import org.osgi.framework.Bundle;

public class ErrorHandler {
	
	private static ErrorHandler m_Instance = new ErrorHandler();
	
	private Journal m_ErrorLog = null;
	private Collection<ErrorManager> m_ErrorManagers = new HashSet<ErrorManager>();
	
	private ErrorHandler(){
		//block instantiation
	}
	
	public static ErrorHandler getInstance(){
		return m_Instance;
	}
	
	public void init(Journal errorLog){
		//only init if the Log is still null
		if(m_ErrorLog == null){
			m_ErrorLog = errorLog;
		}
	}
	
	public synchronized void handleWarning(String warning, Bundle thrower){
		System.err.println("Warning: "+warning);
		if(m_ErrorLog != null){
			m_ErrorLog.write(thrower, " "+warning);
		}
		for(ErrorManager em : m_ErrorManagers){
			em.handleWarning(warning, thrower);
		}
	}
	
	public synchronized void handleException(Exception e, Bundle thrower){
		System.err.println("Error: "+e.getMessage());
		if(m_ErrorLog != null){
			if(Debug.getLevel() == Debug.FULL){
				String trace = "";
				for(StackTraceElement elem : e.getStackTrace()){
					trace += elem + "\n";
				}
				m_ErrorLog.write(thrower, " ERROR: "+e.getMessage()+"\n"+trace);
			}else{
				m_ErrorLog.write(thrower, " ERROR: "+e.getMessage());
			}
		}
		for(ErrorManager em : m_ErrorManagers){
			em.handleException(e, thrower);
		}
	}
	
	public synchronized void addErrorManager(ErrorManager manager){
		m_ErrorManagers.add(manager);
	}
	
	public synchronized void removeErrorManager(ErrorManager manager){
		m_ErrorManagers.remove(manager);
	}
	
}
