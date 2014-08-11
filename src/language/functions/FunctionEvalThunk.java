package language.functions;

public class FunctionEvalThunk extends Thunk{
	
	private final Function func;
	
	public FunctionEvalThunk(Function func){
		this.func = func;
	}
	
	@Override
	protected Object eval() {
		return func.evaluate();
	}

}
