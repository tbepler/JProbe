package language.functions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import language.exceptions.InvalidArgumentException;
import language.exceptions.MissingArgsException;
import language.parser.Visitor;
import language.parser.framework.Node;
import language.symboltable.Kinds;
import language.symboltable.PersistentQueue;
import language.symboltable.PersistentTreeMap;
import language.symboltable.Symbol;
import language.symboltable.SymbolTable;

public class DefinedFunction implements Function{
	
	private final SymbolTable<FunctionBuilder> innerFunctions;
	private Scope scope = null;
	private final PersistentQueue<String> params;
	private final Node<Visitor> def;
	
	public DefinedFunction(SymbolTable<FunctionBuilder> innerFunctions, 
			PersistentQueue<String> params, Node<Visitor> def){
		this.innerFunctions = innerFunctions;
		this.params = params;
		this.def = def;
	}
	
	@Override
	public DefinedFunction setScope(Scope s){
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
		return new DefinedFunction(innerFunctions, remainingParams, def).setScope(newScope);
	}

	@Override
	public Object evaluate() throws MissingArgsException {
		//build the inner functions and add them to the scope
		Scope nested = scope;
		Set<Function> funcs = new HashSet<Function>();
		Map<Symbol, Collection<FunctionBuilder>> local = innerFunctions.getCurrentTable();
		for(Symbol s : local.keySet()){
			if(s.kind() == Kinds.FUNCTION){
				for(FunctionBuilder b : local.get(s)){
					Function f = b.define();
					funcs.add(f);
					nested = nested.insert(s.identifier(), new ValueThunk(f));
				}
			}
		}
		for(Function f : funcs){
			f.setScope(nested);
		}
		EvaluationVisitor eval = new EvaluationVisitor(nested);
		def.accept(eval);
		return eval.result().exec();
	}

	@Override
	public int params() {
		return params.size();
	}

}
