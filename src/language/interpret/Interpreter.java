package language.interpret;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;

import language.functions.EvaluationVisitor;
import language.functions.Function;
import language.functions.FunctionBuilder;
import language.functions.Scope;
import language.functions.SymbolTableFiller;
import language.functions.Thunk;
import language.functions.ValueThunk;
import language.parser.ParsingEngineImpl;
import language.parser.TokenFactoryImpl;
import language.parser.Visitor;
import language.parser.framework.BurkeFischerErrorRepair;
import language.parser.framework.Lexer;
import language.parser.framework.Node;
import language.parser.framework.Parser;
import language.parser.framework.TokenFactory;
import language.parser.nodes.Add;
import language.parser.nodes.Assign;
import language.parser.nodes.Divide;
import language.parser.nodes.FApply;
import language.parser.nodes.False;
import language.parser.nodes.IdListAppend;
import language.parser.nodes.IdListHead;
import language.parser.nodes.Multiply;
import language.parser.nodes.Negate;
import language.parser.nodes.Not;
import language.parser.nodes.Power;
import language.parser.nodes.Subtract;
import language.parser.nodes.True;
import language.parser.nodes._Punctuation9exp_Punctuation10exp;
import language.parser.nodes.assignprog;
import language.parser.nodes.binopexp;
import language.parser.nodes.errorToken;
import language.parser.nodes.expprog;
import language.parser.nodes.floatToken;
import language.parser.nodes.floatexp;
import language.parser.nodes.idToken;
import language.parser.nodes.idexp;
import language.parser.nodes.intToken;
import language.parser.nodes.intexp;
import language.parser.nodes.uopexp;
import language.symboltable.SymbolTable;

public class Interpreter implements Visitor{

	private Scope env = new Scope();
	private final SymbolTable<FunctionBuilder> defs = new SymbolTable<FunctionBuilder>();
	private final Parser<Visitor> parse = new Parser<Visitor>(new ParsingEngineImpl());
	private final TokenFactory<Visitor> fac = new TokenFactoryImpl();
	
	public void interpret(InputStream in) throws IOException{
		Lexer<Visitor> lexer = new Lexer<Visitor>(in, fac);
		Node<Visitor> astRoot = parse.parse(lexer, new BurkeFischerErrorRepair<Visitor>(10, 4));
		if(astRoot != null){
			astRoot.accept(new SymbolTableFiller(defs));
			astRoot.accept(this);
			while(!memo.isEmpty()){
				Object o = memo.pop();
				if(o instanceof Thunk){
					memo.push(((Thunk) o).exec());
				}else{
					System.out.println(o);
				}
			}
		}
	}
	
	private Deque<Object> memo = new ArrayDeque<Object>();

	@Override
	public void visit(idToken node) {
		memo.push(node.getText());
	}

	@Override
	public void visit(intToken node) {
		memo.push(Integer.parseInt(node.getText()));
	}

	@Override
	public void visit(floatToken node) {
		memo.push(Double.parseDouble(node.getText()));
	}

	@Override
	public void visit(errorToken node) {
		throw new RuntimeException("Error on node: "+node);
	}

	@Override
	public void visit(Assign node) {
		node.idlist0.accept(this);
		FunctionBuilder build = new FunctionBuilder(defs);
		String name = (String) memo.pollLast();
		while(!memo.isEmpty()){
			build.appendParam((String) memo.pollLast());
		}
		build.assign(node.exp1);
		Function f = build.define();
		env = env.insert(name, new ValueThunk(f));
		f.setScope(env);
	}

	@Override
	public void visit(idexp node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(intexp node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(floatexp node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(True node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(False node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());	
	}

	@Override
	public void visit(binopexp node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(uopexp node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());	}

	@Override
	public void visit(FApply node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(Add node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(Subtract node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(Multiply node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(Divide node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(Power node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(Negate node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(Not node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}

	@Override
	public void visit(IdListHead node) {
		node.id0.accept(this);
	}

	@Override
	public void visit(IdListAppend node) {
		node.idlist0.accept(this);
		node.id1.accept(this);
	}

	@Override
	public void visit(assignprog node) {
		node.assign0.accept(this);
	}

	@Override
	public void visit(expprog node) {
		node.exp0.accept(this);
	}
	
	@Override
	public void visit(_Punctuation9exp_Punctuation10exp node) {
		EvaluationVisitor eval = new EvaluationVisitor(env);
		node.accept(eval);
		memo.push(eval.result());
	}
	
}
