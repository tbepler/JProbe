package language.functions;

import java.util.ArrayList;
import java.util.List;

import language.parser.Visitor;
import language.parser.framework.Node;
import language.symboltable.PersistentQueue;
import language.symboltable.SymbolTable;

public class FunctionBuilder {
	
	private final SymbolTable<FunctionBuilder> parentTable;
	private final List<String> params = new ArrayList<String>();
	private SymbolTable<FunctionBuilder> symbolTable = null;
	private Node<Visitor> definition = null;
	
	public FunctionBuilder(SymbolTable<FunctionBuilder> parentTable){
		this.parentTable = parentTable;
	}
	
	public FunctionBuilder appendParam(String param){
		params.add(param);
		return this;
	}
	
	public FunctionBuilder assign(Node<Visitor> val){
		definition = val;
		symbolTable = new SymbolTable<FunctionBuilder>(parentTable);
		SymbolTableFiller fill = new SymbolTableFiller(symbolTable);
		definition.accept(fill);
		return this;
	}
	
	public Function define(){
		return new DefinedFunction(symbolTable,
				new PersistentQueue<String>(params), definition);
	}
	
}
