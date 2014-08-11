package language.functions;

import language.parser.Visitor;
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

public class FunctionVisitor implements Visitor{

	@Override
	public void visit(idToken node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(intToken node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(floatToken node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(errorToken node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(idlist_Punctuation5expassign node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(idexp node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(intexp node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(floatexp node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(True node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(False node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(binopexp node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(uopexp node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FApply node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(_Punctuation8exp_Punctuation9exp node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Add node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Subtract node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Multiply node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Divide node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Power node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Negate node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Not node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IdListHead node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IdListAppend node) {
		// TODO Auto-generated method stub
		
	}

}
