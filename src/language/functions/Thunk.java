package language.functions;

public abstract class Thunk {
	
	private Object memo;
	private boolean computed = false;
	
	protected abstract Object eval();
	
	public Object exec(){
		if(!computed){
			memo = this.eval();
			computed = true;
		}
		return memo;
	}
	
	
}
