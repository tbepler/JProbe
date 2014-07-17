package jprobe.framework.model.compiler;

import java.io.Serializable;
import java.util.Arrays;

public final class Element implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final Token m_Token;
	private final SyntaxElement m_Node;
	private final int m_Hash;
	
	public Element(Token t){
		if(t == null){
			throw new NullPointerException();
		}
		m_Token = t;
		m_Node = null;
		m_Hash = Arrays.hashCode(new Object[]{m_Token,m_Node});
	}
	
	public Element(SyntaxElement e){
		if(e == null){
			throw new NullPointerException();
		}
		m_Node = e;
		m_Token = null;
		m_Hash = Arrays.hashCode(new Object[]{m_Token,m_Node});
	}
	
	@Override
	public String toString(){
		if(m_Token != null){
			return m_Token.toString();
		}
		return m_Node.toString();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Element){
			Element e = (Element) o;
			if(m_Token == null && e.m_Token == null){
				return m_Node.equals(e.m_Node);
			}
			if(m_Node == null && e.m_Node == null){
				return m_Token.equals(e.m_Token);
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	public boolean isToken(){
		return m_Token != null;	
	}
	
	public boolean isSyntaxElement(){
		return m_Node != null;
	}
	
	public Token getToken(){
		return m_Token;
	}
	
	public SyntaxElement getSyntaxElement(){
		return m_Node;
	}
	
}
