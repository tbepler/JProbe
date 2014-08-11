package language.functions;

import language.exceptions.InvalidArgumentException;
import language.exceptions.MissingArgsException;
import language.parser.Visitor;
import language.parser.framework.Node;
import language.symboltable.PersistentQueue;
import language.symboltable.PersistentTreeMap;

public class DefinedFunction implements Function{
	
	private final Scope scope;
	private final PersistentTreeMap<String, Thunk> args;
	private final PersistentQueue<String> params;
	private final Node<Visitor> def;
	
	public DefinedFunction(Scope scope, PersistentTreeMap<String, Thunk> args,
			PersistentQueue<String> params, Node<Visitor> def){
		this.scope = scope;
		this.args = args;
		this.params = params;
		this.def = def;
	}

	@Override
	public Function apply(Thunk arg) throws InvalidArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object evaluate() throws MissingArgsException {
		// TODO Auto-generated method stub
		return null;
	}

}
