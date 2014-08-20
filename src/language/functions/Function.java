package language.functions;

import language.exceptions.InvalidArgumentException;
import language.exceptions.MissingArgsException;


/**
 * 
 * @author Wei, Tristan
 *
 */

public interface Function {
	
	public int params();
	
	public Function apply(Thunk arg) throws InvalidArgumentException;
	
	public Object evaluate() throws MissingArgsException;
	
	public Function setScope(Scope s);
	
}
