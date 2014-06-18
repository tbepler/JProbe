package jprobe.save;

import java.io.Serializable;

public class Tag implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final String m_Id;
	private final long m_Bytes;
	
	public Tag(String id, long numBytes){
		m_Id = id;
		m_Bytes = numBytes;
	}
	
	public String getId(){
		return m_Id;
	}
	
	public long getNumBytes(){
		return m_Bytes;
	}

}
