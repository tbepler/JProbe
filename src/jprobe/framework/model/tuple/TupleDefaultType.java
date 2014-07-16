package jprobe.framework.model.tuple;

import jprobe.framework.model.types.Type;

public class TupleDefaultType extends TupleType<TupleDefault>{
	private static final long serialVersionUID = 1L;
	
	
	
	public TupleDefaultType(Object... objs) {
		super(objs);
	}


	public TupleDefaultType(Type<?>... types) {
		super(types);
	}


	@Override
	public TupleDefault newInstance(Object... objs) {
		if(objs.length == this.size()){
			for(int i=0; i<objs.length; ++i){
				objs[i] = this.get(i).cast(objs[i]);
			}
			return new TupleDefault(objs);
		}
		throw new InstantiationException("Type: "+this+" cannot be instantiated from "+objs.length+" arguments.");
	}

}
