package jprobe.framework.model.tuple;

public class TupleDefault extends Tuple{
	private static final long serialVersionUID = 1L;
	
	private final TupleDefaultType m_Type;
	
	public TupleDefault(Object ... objs){
		super(objs);
		m_Type = new TupleDefaultType(objs);
	}

	TupleDefault(TupleDefaultType type, Object objs){
		super(objs);
		m_Type = type;
	}
	
	@Override
	public TupleDefaultType getType() {
		return m_Type;
	}

}
