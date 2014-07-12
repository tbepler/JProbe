package jprobe.framework.model.types;

import jprobe.framework.model.function.Function;
import jprobe.framework.model.function.InvocationException;
import jprobe.framework.model.function.Procedure;
import jprobe.framework.model.function.TypeMismatchException;
import jprobe.framework.model.tuple.Tuple;

/**
 * A procedure that boxes parameters into tuples
 * 
 * @author Tristan Bepler
 *
 */
public class BoxingOperation implements Procedure<Tuple, BoxingOperation>{
	private static final long serialVersionUID = 1L;
	
	public BoxingOperation(TupleClass targetType){
		//construct the boxing signature
		//TODO
	}
	
	@Override
	public Signature<Tuple, BoxingOperation> getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple invoke(Function<?, ?>... args)
			throws IllegalArgumentException, TypeMismatchException,
			InvocationException {
		// TODO Auto-generated method stub
		return null;
	}

}
