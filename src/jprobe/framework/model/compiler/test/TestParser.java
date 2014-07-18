package jprobe.framework.model.compiler.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jprobe.framework.model.compiler.Grammar;
import jprobe.framework.model.compiler.Parser;
import jprobe.framework.model.compiler.Production;

public class TestParser extends junit.framework.TestCase{
	
	private static enum Symbols{
		
		Z(false),
		Y(false),
		X(false),
		a(true),
		c(true),
		d(true);
		
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
	
}
