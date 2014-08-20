package language.functions.core;

import java.util.ArrayList;
import java.util.Collection;
import language.exceptions.InvalidArgumentException;
import language.exceptions.MissingArgsException;
import language.functions.Function;
import language.functions.Scope;
import language.functions.Thunk;
import language.symboltable.PersistentQueue;

public class CoreFunction implements Function{
	
	private Scope scope = null;
	private final PersistentQueue<String> params;
	private final Procedure proc;
	
	public CoreFunction(Procedure proc){
		this.proc = proc;
		Collection<String> list = new ArrayList<String>();
		for( int i = 0 ; i < proc.params() ; ++i ){
			list.add(String.valueOf(i));
		}
		params = new PersistentQueue<String>(list);
	}
	
	private CoreFunction(Procedure proc, PersistentQueue<String> params){
		this.proc = proc;
		this.params = params;
	}
	
	@Override
	public CoreFunction setScope(Scope s){
		this.scope = s;
		return this;
	}

	@Override
	public Function apply(Thunk arg) throws InvalidArgumentException {
		if(params.isEmpty()){
			throw new InvalidArgumentException("Takes no arguments but received argument: "+arg);
		}
		String param = params.peek();
		Scope newScope = scope.insert(param, arg);
		PersistentQueue<String> remainingParams = params.remove();
		return new CoreFunction(proc, remainingParams).setScope(newScope);
	}
	
	private Object closure(Thunk t){
		Object o = t.exec();
		while(o instanceof Thunk){
			t = (Thunk) o;
			o = t.exec();
		}
		return o;
	}

	@Override
	public Object evaluate() throws MissingArgsException {
		//build the inner functions and add them to the scope
		Object[] args = new Object[proc.params()];
		for( int i = 0 ; i < args.length ; ++i ){
			args[i] = closure(scope.lookup(String.valueOf(i)));
		}
		return proc.exec(args);
	}

	@Override
	public int params() {
		return params.size();
	}

}
