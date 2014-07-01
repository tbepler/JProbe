package jprobe.services;

import java.util.Collection;
import java.util.HashSet;

import org.osgi.framework.Bundle;

import util.logging.Log;

public class ErrorHandler {
	
	private static ErrorHandler m_Instance = new ErrorHandler();
	
	private Log m_ErrorLog = null;
	private Collection<ErrorManager> m_ErrorManagers = new HashSet<ErrorManager>();
	
	private ErrorHandler(){
		//block instantiation
	}
	
	public static ErrorHandler getInstance(){
		return m_Instance;
	}
	
	public void init(Log errorLog){
		//only init if the Log is still null
		if(m_ErrorLog == null){
			m_ErrorLog = errorLog;
		}
	}
	
	public synchronized void handleWarning(String warning, Bundle thrower){
		System.err.println("Warning: "+warning);
		if(m_ErrorLog != null){
			StringBuilder builder = new StringBuilder();
			builder.append("<");
			if(thrower != null){
				builder.append(thrower.getSymbolicName());
			}else{
				builder.append("UnknownBundle");
			}
			builder.append(">").append(" Warning: ").append(warning);
			m_ErrorLog.write(builder.toString());
		}
		for(ErrorManager em : m_ErrorManagers){
			em.handleWarning(warning, thrower);
		}
	}
	
	public synchronized void handleException(Exception e, Bundle thrower){
		System.err.println("Error: "+e.getMessage());
		if(m_ErrorLog != null){
			StringBuilder builder = new StringBuilder();
			builder.append("<");
			if(thrower != null){
				builder.append(thrower.getSymbolicName());
			}else{
				builder.append("UnknownBundle");
			}
			builder.append(">").append(" Error: ").append(e.getMessage());
			if(Debug.getLevel() == Debug.FULL){
				builder.append("\n");
				for(StackTraceElement elem : e.getStackTrace()){
					builder.append(elem).append("\n");
				}
			}
			m_ErrorLog.write(builder.toString());
		}
		for(ErrorManager em : m_ErrorManagers){
			em.handleException(e, thrower);
		}
	}
	
	public synchronized void handleError(String errorMessage, Bundle thrower){
		System.err.println("Error: "+errorMessage);
		if(m_ErrorLog != null){
			StringBuilder builder = new StringBuilder();
			builder.append("<");
			if(thrower != null){
				builder.append(thrower.getSymbolicName());
			}else{
				builder.append("UnknownBundle");
			}
			builder.append(">").append(" Error: ").append(errorMessage);
			m_ErrorLog.write(builder.toString());
		}
		for(ErrorManager em : m_ErrorManagers){
			em.handleError(errorMessage, thrower);
		}
	}
	
	public synchronized void addErrorManager(ErrorManager manager){
		m_ErrorManagers.add(manager);
	}
	
	public synchronized void removeErrorManager(ErrorManager manager){
		m_ErrorManagers.remove(manager);
	}
	
}
