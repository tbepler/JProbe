package language.implementation.symbols.lists;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Token;
import language.implementation.Equals;
import language.implementation.Visitor;
import language.implementation.symbols.Statement;

public class StmList extends Statement{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Token<Visitor>>> RHS = 
			ListUtil.<Class<? extends Token<Visitor>>>asUnmodifiableList(Statement.class, Statement.class);

	public final Statement left, right;
	
	public StmList(Statement left, Statement right){ this.left = left; this.right = right; }
	
	@Override
	public Assoc getAssoc(){
		return Assoc.LEFT;
	}
	
	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == RHS.size());
		for( int i = 0 ; i < RHS.size() ; ++i ){
			assert(RHS.get(i).isAssignableFrom(symbols.get(i).getClass()));
		}
		return new StmList((Statement) symbols.get(0), (Statement) symbols.get(1));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public int hashCode(){
		return Equals.hashCode(left, right);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof StmList){
			StmList that = (StmList) o;
			return Equals.equals(this.left, that.left) && Equals.equals(this.right, that.right);
		}
		return false;
	}

}
