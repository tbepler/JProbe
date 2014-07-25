package language.implementation;

import java.util.ArrayList;
import java.util.List;

import language.compiler.grammar.Production;
import language.compiler.grammar.TokenFactory;
import language.implementation.symbols.EOF;
import language.implementation.symbols.ErrorToken;
import language.implementation.symbols.Program;
import language.implementation.symbols.Terminal;
import language.implementation.symbols.Whitespace;
import language.implementation.symbols.declarations.ParamDecl;
import language.implementation.symbols.declarations.FuncDecl;
import language.implementation.symbols.declarations.TypeSignDecl;
import language.implementation.symbols.expressions.AssignFunctionExp;
import language.implementation.symbols.expressions.AssignVarExp;
import language.implementation.symbols.expressions.BooleanExp;
import language.implementation.symbols.expressions.CharExp;
import language.implementation.symbols.expressions.DivExp;
import language.implementation.symbols.expressions.DoubleExp;
import language.implementation.symbols.expressions.ExponentExp;
import language.implementation.symbols.expressions.FunctionExp;
import language.implementation.symbols.expressions.IdentifierExp;
import language.implementation.symbols.expressions.IntExp;
import language.implementation.symbols.expressions.MinusExp;
import language.implementation.symbols.expressions.MultExp;
import language.implementation.symbols.expressions.PlusExp;
import language.implementation.symbols.expressions.StringExp;
import language.implementation.symbols.lists.IdentifierList;
import language.implementation.symbols.lists.IdentifierListAppend;
import language.implementation.symbols.lists.StmList;
import language.implementation.symbols.statements.DeclStm;
import language.implementation.symbols.statements.ExpStm;
import language.implementation.symbols.terminals.Arrow;
import language.implementation.symbols.terminals.Assign;
import language.implementation.symbols.terminals.BooleanLiteral;
import language.implementation.symbols.terminals.CharLiteral;
import language.implementation.symbols.terminals.Comma;
import language.implementation.symbols.terminals.Div;
import language.implementation.symbols.terminals.DoubleColon;
import language.implementation.symbols.terminals.DoubleLiteral;
import language.implementation.symbols.terminals.Exponent;
import language.implementation.symbols.terminals.FunKeyword;
import language.implementation.symbols.terminals.Identifier;
import language.implementation.symbols.terminals.IntLiteral;
import language.implementation.symbols.terminals.LParen;
import language.implementation.symbols.terminals.Minus;
import language.implementation.symbols.terminals.Mult;
import language.implementation.symbols.terminals.Plus;
import language.implementation.symbols.terminals.RParen;
import language.implementation.symbols.terminals.Semicolon;
import language.implementation.symbols.terminals.StringLiteral;
import language.implementation.symbols.types.IdType;

public class SabreTokenFactory implements TokenFactory<Visitor>{	
	
	

	@Override
	public Terminal newEOFToken() {
		return new EOF();
	}
	
	@Override
	public Production<Visitor> newStartProduction() {
		return new Program(null);
	}

	@Override
	public List<Production<Visitor>> newProductions() {
		List<Production<Visitor>> list = new ArrayList<Production<Visitor>>();
		list.add(new Program(null));
		list.add(new StmList(null, null));
		list.add(new ExpStm(null));
		list.add(new DeclStm(null));
		list.add(new IdentifierList());
		list.add(new IdentifierListAppend());
		list.add(new AssignVarExp(null, null));
		list.add(new AssignFunctionExp(null, null));
		list.add(new ExponentExp(null, null));
		list.add(new PlusExp(null, null));
		list.add(new MinusExp(null, null));
		list.add(new MultExp(null, null));
		list.add(new DivExp(null, null));
		list.add(new BooleanExp(null));
		list.add(new CharExp(null));
		list.add(new DoubleExp(null));
		list.add(new IdentifierExp(null));
		list.add(new IntExp(null));
		list.add(new StringExp(null));
		list.add(new FunctionExp(null, null));
		list.add(new FuncDecl(null));
		list.add(new ParamDecl(null, null));
		list.add(new TypeSignDecl(null, null, null));
		list.add(new IdType(null));
		//list.add(new ArgumentApplication(null, null));
		//list.add(new IdentifierCall(null));
		return list;
	}

	@Override
	public List<Terminal> newTerminals() {
		List<Terminal> list = new ArrayList<Terminal>();
		list.add(new Assign());
		list.add(new Exponent());
		list.add(new Plus());
		list.add(new Minus());
		list.add(new Mult());
		list.add(new Div());
		list.add(new Arrow());
		list.add(new DoubleColon());
		list.add(new FunKeyword());
		list.add(new BooleanLiteral(false));
		list.add(new CharLiteral(' '));
		list.add(new Comma());
		list.add(new DoubleLiteral(1));
		list.add(new IntLiteral(1));
		list.add(new LParen());
		list.add(new RParen());
		list.add(new Semicolon());
		list.add(new StringLiteral(""));
		list.add(new Identifier(""));
		list.add(new Whitespace());
		list.add(new ErrorToken(""));
		return list;
		
	}

}
