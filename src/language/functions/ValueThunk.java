package language.functions;

public class ValueThunk extends Thunk{
	
	private final Object val;
	
	public ValueThunk(Object val){
		this.val = val;
	}
	
	@Override
	protected Object eval() {
		if(val instanceof Function){
			Function f = (Function) val;
			if(f.params() == 0){
				return f.evaluate();
			}
		}
		return val;
	}

}
