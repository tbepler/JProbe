package language.functions;

import java.util.ArrayList;
import java.util.List;

import language.parser.framework.Node;
import language.symboltable.SymbolTable;

public class FunctionBuilder<V> {
	
	private final List<String> params = new ArrayList<String>();
	private Node<V> definition = null;
	
	public FunctionBuilder<V> appendParam(String param){
		params.add(param);
		return this;
	}
	
	public FunctionBuilder<V> assign(Node<V> val){
		definition = val;
		return this;
	}
	
	public Function define(SymbolTable<FunctionBuilder<V>> scope){
		//TODO
		return null;
	}
	
}
