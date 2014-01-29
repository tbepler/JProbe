package jprobe.save;

import java.io.Serializable;

public class Tag implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final String m_Id;
	private final int m_Bytes;
	
	public Tag(String id, int numBytes){
		m_Id = id;
		m_Bytes = numBytes;
	}
	
	public String getId(){
		return m_Id;
	}
	
	public int getNumBytes(){
		return m_Bytes;
	}

}
