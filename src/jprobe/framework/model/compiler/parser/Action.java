package jprobe.framework.model.compiler.parser;

import java.util.Arrays;

public class Action<S> {
	
	private final State<S> m_Next;
	private final Production<S> m_Prod;
	private final Actions m_Id;
	private final int m_Hash;
	
	public Action(Actions id){
		m_Id = id;
		m_Next = null;
		m_Prod = null;
		m_Hash = Arrays.hashCode(new Object[]{m_Id, m_Next, m_Prod});
	}
	
	public Action(Actions id, State<S> next){
		m_Id = id;
		m_Next = next;
		m_Prod = null;
		m_Hash = Arrays.hashCode(new Object[]{m_Id, m_Next, m_Prod});
	}
	
	public Action(Actions id, Production<S> prod){
		m_Id = id;
		m_Next = null;
		m_Prod = prod;
		m_Hash = Arrays.hashCode(new Object[]{m_Id, m_Next, m_Prod});
	}
	
	public Actions id(){
		return m_Id;
	}
	
	public State<S> nextState(){
		return m_Next;
	}
	
	public Production<S> production(){
		return m_Prod;
	}
	
	@Override
	public String toString(){
		return m_Id.toString();
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Action){
			Action<?> a = (Action<?>) o;
			if(m_Next == null){
				return m_Id.equals(a.m_Id) && m_Prod.equals(a.m_Prod);
			}else{
				return m_Id.equals(a.m_Id) && m_Next.equals(a.m_Next);
			}
		}
		return false;
	}
	
}
