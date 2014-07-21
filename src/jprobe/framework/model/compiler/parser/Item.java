package jprobe.framework.model.compiler.parser;

import java.util.Arrays;

public class Item<S> {
	
	public static <S> Item<S> forProduction(Production<S> prod){
		return new Item<S>(prod);
	}
	
	private final Production<S> m_Prod;
	private final S[] m_RHS;
	private final S m_LHS;
	private final int m_Index;
	private final int m_Hash;
	
	public Item(Production<S> prod){
		this(0, prod);
	}
	
	public Item(int index, Production<S> prod){
		this(index, prod, prod.leftHandSide(), prod.rightHandSide());
	}
	
	private Item(int index, Production<S> prod, S lhs, S ... rhs){
		m_Index = index;
		m_Prod = prod;
		m_RHS = rhs;
		m_LHS = lhs;
		m_Hash = Arrays.hashCode(new Object[]{m_Index, m_Prod});
	}
	
	public Production<S> getProduction(){
		return m_Prod;
	}
	
	public Item<S> increment(){
		return new Item<S>(m_Index+1, m_Prod, m_LHS, m_RHS);
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
			return m_Index == i.m_Index && m_Prod.equals(i.m_Prod);
		}
		return false;
	}
	
}
