package jprobe.framework.model.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Item<S> {
	
	public static <S> Item<S> forProduction(Production<S> prod, S ... lookahead){
		return new Item<S>(prod, lookahead);
	}
	
	private final Production<S> m_Prod;
	private final S[] m_RHS;
	private final S m_LHS;
	private final S[] m_Lookahead;
	private final int m_Index;
	private final int m_Hash;
	
	public Item(Production<S> prod, S ... lookahead){
		this(0, prod, lookahead);
	}
	
	public Item(int index, Production<S> prod, S ... lookahead){
		this(index, lookahead, prod, prod.leftHandSide(), prod.rightHandSide());
	}
	
	protected Item(int index, S[] lookahead, Production<S> prod, S lhs, S ... rhs){
		m_Index = index;
		m_Lookahead = lookahead;
		m_Prod = prod;
		m_RHS = rhs;
		m_LHS = lhs;
		m_Hash = Arrays.deepHashCode(new Object[]{m_Index, m_Prod, m_Lookahead});
	}
	
	public Production<S> getProduction(){
		return m_Prod;
	}
	
	public Item<S> increment(){
		return new Item<S>(m_Index+1, m_Lookahead, m_Prod, m_LHS, m_RHS);
	}
	
	public List<S> alpha(){
		List<S> alpha = new ArrayList<S>();
		for(int i=0; i<m_Index; ++i){
			alpha.add(m_RHS[i]);
		}
		return alpha;
	}
	
	public List<S> beta(){
		List<S> beta = new ArrayList<S>();
		for(int i=m_Index+1; i<m_RHS.length; ++i){
			beta.add(m_RHS[i]);
		}
		return beta;
	}
	
	public boolean hasNext(){
		return m_Index < m_RHS.length;
	}
	
	public S next(){
		return m_RHS[m_Index];
	}
	
	public boolean hasPrev(){
		return m_Index > 0;
	}
	
	public S prev(){
		return m_RHS[m_Index-1];
	}
	
	public int lookaheadLength(){
		return m_Lookahead.length;
	}
	
	public S lookahead(int index){
		return m_Lookahead[index];
	}
	
	public List<S> lookahead(){
		return Arrays.asList(m_Lookahead.clone());
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Item){
			Item<?> i = (Item<?>) o;
			return m_Index == i.m_Index && m_Prod.equals(i.m_Prod) && Arrays.deepEquals(m_Lookahead, i.m_Lookahead);
		}
		return false;
	}
	
}
