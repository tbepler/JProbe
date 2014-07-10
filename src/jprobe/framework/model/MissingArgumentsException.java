package jprobe.framework.model;

import java.util.Collection;

public class MissingArgumentsException extends Exception{
	private static final long serialVersionUID = 1L;
	
	private final Function<?> m_Func;
	private final int m_Received;
	private final int m_Required;
	
	public MissingArgumentsException(Function<?> func, int receivedArgs, int requiredArgs){
		super(generateMessage(func, receivedArgs, requiredArgs));
	}
	
	public Function<?> getFunction(){
		return m_Func;
	}
	
	public int numArgsRecieved(){
		return m_Received;
	}
	
	public int numArgsRequired(){
		return m_Required;
	}
	
	private static String generateMessage(Function<?> f, int receivedArgs, int requiredArgs){
		StringBuilder builder = new StringBuilder();
		builder.append("Function ").append(f).append(": ");
		builder.append("received ").append(receivedArgs);
		builder.append(" args but requires ").append(requiredArgs).append(" args.");
		return builder.toString();
	}

}
