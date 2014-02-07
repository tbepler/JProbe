package plugins.genome.services.reader;

import java.io.Serializable;

import plugins.genome.services.utils.GenomicRegion;

public abstract class SequenceQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final String m_Target;
	
	protected SequenceQuery(String targetSequence){
		m_Target = targetSequence;
	}
	
	public String getTargetSequence(){
		return m_Target;
	}
	
	public abstract void process(GenomicRegion location);
	
}