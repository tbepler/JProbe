package plugins.genome.services.reader;

import plugins.genome.services.utils.Chromosome;

public abstract class LocationQuery {
	
	private Chromosome m_Chr;
	private long m_Start;
	private long m_End;
	
	protected LocationQuery(Chromosome chr, long start, long end){
		m_Chr = chr;
		m_Start = start;
		m_End = end;
	}
	
	protected LocationQuery(Chromosome chr, int start, int end){
		this(chr, (long) start, (long) end);
	}
	
	public Chromosome getChromosome(){
		return m_Chr;
	}
	
	public long getStart(){
		return m_Start;
	}
	
	public long getEnd(){
		return m_End;
	}
	
	public abstract void process(String locationSequence);
	
}
