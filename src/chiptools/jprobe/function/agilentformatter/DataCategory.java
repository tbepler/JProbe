package chiptools.jprobe.function.agilentformatter;

import java.io.Serializable;

import jprobe.services.data.Data;

public class DataCategory<D extends Data> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final String CATEGORY;
	public final D DATA;
	
	public DataCategory(String category, D data){
		this.CATEGORY = category;
		this.DATA = data;
	}
	
}
