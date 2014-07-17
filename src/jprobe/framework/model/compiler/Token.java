package jprobe.framework.model.compiler;

public class Token {
	
	private String m_Str;
	
	public Token(String s){
		m_Str = s;
	}
	
	public String getString(){
		return m_Str;
	}
	
	public int start(){
		//TODO
		return 0;
	}
	
	public int length(){
		//TODO
		return 0;
	}
	
}
