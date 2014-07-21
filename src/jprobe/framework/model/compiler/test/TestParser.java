package jprobe.framework.model.compiler.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jprobe.framework.model.compiler.parser.Action;
import jprobe.framework.model.compiler.parser.Actions;
import jprobe.framework.model.compiler.parser.Grammar;
import jprobe.framework.model.compiler.parser.Item;
import jprobe.framework.model.compiler.parser.Parser;
import jprobe.framework.model.compiler.parser.Production;
import jprobe.framework.model.compiler.parser.State;

public class TestParser extends junit.framework.TestCase{
	
	private static enum Symbols{
		
		Z(false),
		Y(false),
		X(false),
		a(true),
		c(true),
		d(true),
		EOF(true),
		Zprime(false);
		
		private final boolean terminal;
		
		private Symbols(boolean terminal){
			this.terminal = terminal;
		}
		
		public boolean isTerminal(){
			return terminal;
		}
		
	}
	
	private static enum Rules implements Production<Symbols>{
		
		Z_d(Symbols.Z, Symbols.d),
		Z_XYZ(Symbols.Z, Symbols.X, Symbols.Y, Symbols.Z),
		Y_(Symbols.Y),
		Y_c(Symbols.Y, Symbols.c),
		X_Y(Symbols.X, Symbols.Y),
		X_a(Symbols.X, Symbols.a);
		
		private final Symbols lhs;
		private final Symbols[] rhs;
		
		private Rules(Symbols lhs, Symbols ... rhs){
			this.lhs = lhs;
			this.rhs = rhs;
		}
		
		@Override
		public Symbols leftHandSide() {
			return lhs;
		}

		@Override
		public Symbols[] rightHandSide() {
			return rhs.clone();
		}
		
	}
	
	private static class SimpleGrammar implements Grammar<Symbols>{

		@Override
		public Collection<Symbols> getTerminalSymbols() {
			Collection<Symbols> terminal = new ArrayList<Symbols>();
			for(Symbols s : Symbols.values()){
				if(s.isTerminal()){
					terminal.add(s);
				}
			}
			return terminal;
		}

		@Override
		public Collection<Production<Symbols>> getProductions(Symbols leftHandSide) {
			Collection<Production<Symbols>> ps = new ArrayList<Production<Symbols>>();
			for(Production<Symbols> p : Rules.values()){
				if(p.leftHandSide() == leftHandSide){
					ps.add(p);
				}
			}
			return ps;
		}

		@Override
		public Collection<Production<Symbols>> getAllProductions() {
			return Arrays.<Production<Symbols>>asList(Rules.values());
		}

		@Override
		public Iterator<Production<Symbols>> iterator() {
			return Arrays.<Production<Symbols>>asList(Rules.values()).iterator();
		}

		@Override
		public boolean isTerminal(Symbols symbol) {
			return symbol.isTerminal();
		}

		@Override
		public Production<Symbols> getEOFStartProduction() {
			return new Production<Symbols>(){

				@Override
				public Symbols leftHandSide() {
					return Symbols.Zprime;
				}

				@Override
				public Symbols[] rightHandSide() {
					return new Symbols[]{Symbols.Z, Symbols.EOF};
				}
				
			};
		}

		@Override
		public boolean isEOFSymbol(Symbols symbol) {
			return symbol == Symbols.EOF;
		}
		
	}
	
	public void testFirstFollowNullable(){
		Parser<Symbols> parser = new Parser<Symbols>(new SimpleGrammar());
		
		//test nullable
		assertTrue(parser.isNullable(Symbols.X));
		assertTrue(parser.isNullable(Symbols.Y));
		assertFalse(parser.isNullable(Symbols.Z));
		assertFalse(parser.isNullable(Symbols.a));
		assertFalse(parser.isNullable(Symbols.c));
		assertFalse(parser.isNullable(Symbols.d));
		
		//test first sets
		Set<Symbols> xfirst = parser.getFirst(Symbols.X);
		assertEquals(2, xfirst.size());
		assertTrue(xfirst.contains(Symbols.a));
		assertTrue(xfirst.contains(Symbols.c));
		
		Set<Symbols> yfirst = parser.getFirst(Symbols.Y);
		assertEquals(1, yfirst.size());
		assertTrue(yfirst.contains(Symbols.c));
		
		Set<Symbols> zfirst = parser.getFirst(Symbols.Z);
		assertEquals(3, zfirst.size());
		assertTrue(zfirst.contains(Symbols.a));
		assertTrue(zfirst.contains(Symbols.c));
		assertTrue(zfirst.contains(Symbols.d));
		
		Set<Symbols> afirst = parser.getFirst(Symbols.a);
		assertEquals(1, afirst.size());
		assertTrue(afirst.contains(Symbols.a));
		
		Set<Symbols> cfirst = parser.getFirst(Symbols.c);
		assertEquals(1, cfirst.size());
		assertTrue(cfirst.contains(Symbols.c));
		
		Set<Symbols> dfirst = parser.getFirst(Symbols.d);
		assertEquals(1, dfirst.size());
		assertTrue(dfirst.contains(Symbols.d));
		
		//test follow sets
		Set<Symbols> xfollow = parser.getFollow(Symbols.X);
		assertEquals(3, xfollow.size());
		assertTrue(xfollow.contains(Symbols.a));
		assertTrue(xfollow.contains(Symbols.c));
		assertTrue(xfollow.contains(Symbols.d));
		
		Set<Symbols> yfollow = parser.getFollow(Symbols.Y);
		assertEquals(3, yfollow.size());
		assertTrue(yfollow.contains(Symbols.a));
		assertTrue(yfollow.contains(Symbols.c));
		assertTrue(yfollow.contains(Symbols.d));
		
		Set<Symbols> zfollow = parser.getFollow(Symbols.Z);
		assertTrue(zfollow.isEmpty());
		
		Set<Symbols> afollow = parser.getFollow(Symbols.a);
		assertTrue(afollow.isEmpty());
		
		Set<Symbols> cfollow = parser.getFollow(Symbols.c);
		assertTrue(cfollow.isEmpty());
		
		Set<Symbols> dfollow = parser.getFollow(Symbols.d);
		assertTrue(dfollow.isEmpty());
	}
	
	private static final Collection<String> TERMINAL = new HashSet<String>(Arrays.asList(
			"x",
			"+"
			));
	
	private static final String EOF = "$";
	private static final String AUXIL_START = "S";
	private static final Production<String> AUXIL_START_PRODUCTION = new Production<String>(){

		@Override
		public String leftHandSide() {
			return AUXIL_START;
		}

		@Override
		public String[] rightHandSide() {
			return new String[]{"E", EOF};
		}
		
	};
	
	private static enum Rules2 implements Production<String>{
		
		E_TpE("E", "T", "+", "E"),
		E_T("E", "T"),
		T_x("T", "x");
		
		private final String lhs;
		private final String[] rhs;
		
		private Rules2(String lhs, String ... rhs){
			this.lhs = lhs;
			this.rhs = rhs;
		}
		
		@Override
		public String leftHandSide() {
			return lhs;
		}

		@Override
		public String[] rightHandSide() {
			return rhs.clone();
		}
		
	}
	
	private static final class Grammar2 implements Grammar<String>{

		@Override
		public Iterator<Production<String>> iterator() {
			return Arrays.<Production<String>>asList(Rules2.values()).iterator();
		}

		@Override
		public Collection<String> getTerminalSymbols() {
			return TERMINAL;
		}

		@Override
		public boolean isTerminal(String symbol) {
			return TERMINAL.contains(symbol);
		}

		@Override
		public Collection<Production<String>> getProductions(String leftHandSide) {
			Collection<Production<String>> list = new ArrayList<Production<String>>();
			for(Production<String> prod : this){
				if(prod.leftHandSide().equals(leftHandSide)){
					list.add(prod);
				}
			}
			return list;
		}

		@Override
		public Collection<Production<String>> getAllProductions() {
			return Arrays.<Production<String>>asList(Rules2.values());
		}

		@Override
		public Production<String> getEOFStartProduction() {
			return AUXIL_START_PRODUCTION;
		}

		@Override
		public boolean isEOFSymbol(String symbol) {
			return EOF.equals(symbol);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE1 = State.forSet(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(AUXIL_START_PRODUCTION),
			new Item<String>(Rules2.E_TpE),
			new Item<String>(Rules2.E_T),
			new Item<String>(Rules2.T_x)
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE2 = State.forSet(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1,AUXIL_START_PRODUCTION)
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE3 = State.forSet(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1, Rules2.E_TpE),
			new Item<String>(1, Rules2.E_T)
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE4 = State.forSet(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(2, Rules2.E_TpE),
			new Item<String>(Rules2.E_TpE),
			new Item<String>(Rules2.E_T),
			new Item<String>(Rules2.T_x)
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE5 = State.forSet(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1, Rules2.T_x)
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE6 = State.forSet(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(3, Rules2.E_TpE)
			)));
	
	public void testActionsTable(){
		Parser<String> parse = new Parser<String>(new Grammar2());
		
		//check the states
		Collection<State<String>> states = parse.getStates();
		assertEquals(6, states.size());
		assertTrue(states.contains(STATE1));
		assertTrue(states.contains(STATE2));
		assertTrue(states.contains(STATE3));
		assertTrue(states.contains(STATE4));
		assertTrue(states.contains(STATE5));
		assertTrue(states.contains(STATE6));
		
		//check the actions table
		Action<String> action = parse.getAction(STATE1, "x");
		assertEquals(Actions.SHIFT, action.id());
		assertEquals(STATE5, action.nextState());
		assertNull(action.production());
		
		action = parse.getAction(STATE1, "+");
		assertNull(action);
		
		action = parse.getAction(STATE1, EOF);
		assertNull(action);
		
		action = parse.getAction(STATE1, "E");
		assertEquals(Actions.GOTO, action.id());
		assertEquals(STATE2, action.nextState());
		assertNull(action.production());
		
		action = parse.getAction(STATE1, "T");
		assertEquals(Actions.GOTO, action.id());
		assertEquals(STATE3, action.nextState());
		assertNull(action.production());
		
		action = parse.getAction(STATE2, EOF);
		assertEquals(Actions.ACCEPT, action.id());
		assertNull(action.nextState());
		assertNull(action.production());
		
		action = parse.getAction(STATE3, "x");
		assertEquals(Actions.REDUCE, action.id());
		assertNull(action.nextState());
		assertEquals(Rules2.E_T, action.production());
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
