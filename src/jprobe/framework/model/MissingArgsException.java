package jprobe.framework.model;

import java.util.Collection;

public class MissingArgsException extends Exception{
	private static final long serialVersionUID = 1L;
	
	private final Function<?> m_Function;
	private final Collection<Parameter<?>> m_Missing;
	
	public MissingArgsException(Function<?> f, Collection<Parameter<?>> params){
		super(generateMessage(f, params));
		m_Function = f;
		m_Missing = params;
	}
	
	public Function<?> getFunction(){
		return m_Function;
	}
	
	public Collection<Parameter<?>> getUndefinedParameters(){
		return m_Missing;
	}
	
	private static String generateMessage(Function<?> f, Collection<Parameter<?>> params){
		StringBuilder builder = new StringBuilder();
		builder.append("Function ").append(f).append(": ");
		builder.append("missing arguments for required parameters: ");
		boolean first = true;
		for(Parameter<?> param : params){
			if(first){
				builder.append(param);
				first = false;
			}else{
				builder.append(", ").append(param);
			}
		}
		return builder.toString();
	}

}
