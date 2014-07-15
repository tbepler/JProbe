package jprobe.system.model;

import java.io.Serializable;

public class Argument implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final int index;
	public final Object value;
	
	public Argument(int paramIndex, Object value){
		this.index  = paramIndex;
		this.value = value;
	}

}
