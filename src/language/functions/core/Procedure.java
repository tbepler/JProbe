package language.functions.core;

public interface Procedure {
	
	public int params();
	
	public Object exec(Object ... args);
	
}
