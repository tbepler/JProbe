package language.functions;

public class ApplyThunk extends Thunk{
	
	private final Thunk func;
	private final Thunk arg;
	
	public ApplyThunk(Thunk func, Thunk arg){
		this.func = func;
		this.arg = arg;
	}
	
	@Override
	protected Object eval() {
		Function f = (Function) func.exec();
		return f.apply(arg);
	}

}
