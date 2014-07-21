package jprobe.framework.model.compiler.parser;

import java.util.Arrays;

public class ProductionImport implements Production<String>{
	
	private final String m_LHS;
	private final String[] m_RHS;
	private final int m_Hash;
	
	public ProductionImport(String lhs, String ... rhs){
		m_LHS = lhs;
		m_RHS = rhs;
		m_Hash = Arrays.deepHashCode(new Object[]{m_LHS, m_RHS});
	}
	
	@Override
	public String leftHandSide() {
		return m_LHS;
	}

	@Override
	public String[] rightHandSide() {
		return m_RHS.clone();
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof ProductionImport){
			ProductionImport p = (ProductionImport) o;
			return m_LHS.equals(p.m_LHS) && Arrays.equals(m_RHS, p.m_RHS);
		}
		return false;
	}

}
