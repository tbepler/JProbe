package language.functions;

import language.exceptions.InvalidArgumentException;
import language.exceptions.MissingArgsException;


/**
 * 
 * @author Wei, Tristan
 *
 */

public interface Function {
	
	public Function apply(Thunk arg) throws InvalidArgumentException;
	
	public Object evaluate() throws MissingArgsException;
	
}
