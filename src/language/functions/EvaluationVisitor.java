package language.functions;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import language.parser.Visitor;
import language.parser.framework.Node;
import language.parser.nodes.Add;
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
import language.parser.nodes._Punctuation8exp_Punctuation9exp;
import language.parser.nodes.binopexp;
import language.parser.nodes.errorToken;
import language.parser.nodes.floatToken;
import language.parser.nodes.floatexp;
import language.parser.nodes.idToken;
import language.parser.nodes.idexp;
import language.parser.nodes.idlist_Punctuation5expassign;
import language.parser.nodes.intToken;
import language.parser.nodes.intexp;
import language.parser.nodes.uopexp;
import language.symboltable.Kinds;
import language.symboltable.Symbol;

public class EvaluationVisitor implements Visitor{
	
	private final Map<Node<Visitor>, Thunk> thunks = new HashMap<Node<Visitor>, Thunk>();
	private final Scope env;
	private final Deque<Thunk> memory = new ArrayDeque<Thunk>();
	
	public EvaluationVisitor(Scope env){
		this.env = env;
	}
	
	public Object result(){
		Thunk t = memory.pop();
		return t.exec();
	}
	
	private Thunk lookup(Node<Visitor> node){
		Thunk t = thunks.get(node);
		if(t == null){
			node.accept(this);
			t = memory.pop();
			thunks.put(node, t);
		}
		return t;
	}
	
	@Override
	public void visit(idToken node) {
		String text = node.getText();
		//TODO type matching
		FunctionBuilder<Visitor> fun = env.lookup(new Symbol(text, Kinds.FUNCTION));
		memory.push(new ValueThunk(fun.define(env)));
	}

	@Override
	public void visit(intToken node) {
		Integer val = Integer.parseInt(node.getText());
		memory.push(new ValueThunk(val));
	}

	@Override
	public void visit(floatToken node) {
		Double val = Double.parseDouble(node.getText());
		memory.push(new ValueThunk(val));
	}

	@Override
	public void visit(errorToken node) {
		throw new RuntimeException("Error on node: "+node);
	}

	@Override
	public void visit(idlist_Punctuation5expassign node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(idexp node) {
		memory.push(lookup(node.id0));
	}

	@Override
	public void visit(intexp node) {
		memory.push(lookup(node.int0));
	}

	@Override
	public void visit(floatexp node) {
		memory.push(lookup(node.float0));
	}

	@Override
	public void visit(True node) {
		memory.push(new ValueThunk(true));
	}

	@Override
	public void visit(False node) {
		memory.push(new ValueThunk(false));
	}

	@Override
	public void visit(binopexp node) {
		memory.push(lookup(node.binop0));
	}

	@Override
	public void visit(uopexp node) {
		memory.push(lookup(node.uop0));
	}

	@Override
	public void visit(FApply node) {
		memory.push(new ApplyThunk(lookup(node.exp0), lookup(node.exp1)));
	}

	@Override
	public void visit(_Punctuation8exp_Punctuation9exp node) {
		memory.push(lookup(node.exp0));
	}

	@Override
	public void visit(final Add node) {
		memory.push( new Thunk(){
			@Override protected Object eval() {
				//TODO define plus operator function
				Function plus = null;
				return plus.apply(lookup(node.exp0)).apply(lookup(node.exp1));
			}
		});
	}

	@Override
	public void visit(final Subtract node) {
		memory.push( new Thunk(){
			@Override protected Object eval() {
				//TODO define subtract operator function
				Function sub = null;
				return sub.apply(lookup(node.exp0)).apply(lookup(node.exp1));
			}
		});
	}

	@Override
	public void visit(final Multiply node) {
		memory.push( new Thunk(){
			@Override protected Object eval() {
				//TODO define mult operator function
				Function mult = null;
				return mult.apply(lookup(node.exp0)).apply(lookup(node.exp1));
			}
		});
	}

	@Override
	public void visit( final Divide node) {
		memory.push( new Thunk(){
			@Override protected Object eval() {
				//TODO define div operator function
				Function div = null;
				return div.apply(lookup(node.exp0)).apply(lookup(node.exp1));
			}
		});
	}

	@Override
	public void visit(final Power node) {
		memory.push( new Thunk(){
			@Override protected Object eval() {
				//TODO define pow operator function
				Function pow = null;
				return pow.apply(lookup(node.exp0)).apply(lookup(node.exp1));
			}
		});
	}

	@Override
	public void visit(final Negate node) {
		memory.push( new Thunk(){
			@Override protected Object eval() {
				//TODO define negate operator function
				Function neg = null;
				return neg.apply(lookup(node.exp0));
			}
		});
	}

	@Override
	public void visit(final Not node) {
		memory.push( new Thunk(){
			@Override protected Object eval() {
				//TODO define not operator function
				Function not = null;
				return not.apply(lookup(node.exp0));
			}
		});
	}

	@Override
	public void visit(IdListHead node) {
		throw new RuntimeException("Cannot evaluate node: "+node);
	}

	@Override
	public void visit(IdListAppend node) {
		throw new RuntimeException("Cannot evaluate node: "+node);
	}
	
	
	
}
