package jprobe.framework.model.types;

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
public class BoxingOperation implements Procedure<Tuple<?>>{
	private static final long serialVersionUID = 1L;
	
	private final Type<?>[] m_Params;
	private final Signature<Tuple<?>> m_Sign;
	
	public BoxingOperation(TupleClass<?> targetType){
		m_Params = targetType.toArray();
		//construct the boxing signature
		m_Sign = new Signature<Tuple<?>>(targetType, m_Params);
	}
	
	@Override
	public Signature<Tuple<?>> getType() {
		return m_Sign;
	}

	@Override
	public Tuple<?> invoke(Procedure<?>... args)
			throws IllegalArgumentException, TypeMismatchException,
			InvocationException {
		
		Object[] vals = new Object[args.length];
		for(int i=0; i<vals.length; ++i){
			vals[i] = m_Params[i].cast(args[i]);
		}
		return new Tuple<Tuple<?>>(vals);
		
	}

	@Override
	public int numParameters() {
		return m_Params.length;
	}

}
