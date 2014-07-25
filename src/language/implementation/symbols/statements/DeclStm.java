package language.implementation.symbols.statements;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Declaration;
import language.implementation.symbols.Statement;
import language.implementation.symbols.terminals.Semicolon;

public class DeclStm extends Statement{
	private static final long serialVersionUID = 1L;

	private static final List<Class<? extends Token<Visitor>>> RHS =
			ListUtil.asUnmodifiableList(Declaration.class, Semicolon.class);
	
	public final Declaration d;
	
	public DeclStm(Declaration d){ this.d = d; }

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == RHS.size());
		for(int i=0; i<RHS.size(); ++i){
			assert(RHS.get(i).isInstance(symbols.get(i)));
		}
		return new DeclStm((Declaration)symbols.get(0));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeclStm other = (DeclStm) obj;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		return true;
	}
	
	

}
