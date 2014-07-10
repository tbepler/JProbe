package jprobe.framework.model;

public class TypeMismatchException extends Exception{
	private static final long serialVersionUID = 1L;
	
	private final Parameter<?> m_Param;
	private final Value<?> m_Value;
	
	public TypeMismatchException(Parameter<?> param, Value<?> value){
		super(generateMessage(param, value));
		m_Param = param;
		m_Value = value;
	}
	
	public Parameter<?> getParameter(){
		return m_Param;
	}
	
	public Value<?> getValue(){
		return m_Value;
	}
	
	private static String generateMessage(Parameter<?> param, Value<?> value){
		StringBuilder builder = new StringBuilder();
		builder.append("Parameter ").append(param).append(" type: ").append(param.getType());
		builder.append(" does not match value ").append(value).append(" type: ").append(value.getType());
		return builder.toString();
	}
	
}
