package util.genome.peak;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import util.genome.GenomicSequence;
import util.genome.ParsingException;
import util.genome.Strand;

public class PeakSequence extends Peak{
	private static final long serialVersionUID = 1L;
	
	public static final String[][] FORMATS = Parser.PEAK_SEQ_FORMATS;
	public static final int ELEMENTS = 9;
	
	public static PeakSequence parsePeakSequence(String s) throws ParsingException{
		return Parser.parsePeakSequence(s);
	}
	
	private final GenomicSequence m_Sequence;
	private final int m_Hash;
	
	public PeakSequence(GenomicSequence sequence, Peak peak){
		super(sequence.getRegion(), peak);
		m_Sequence = sequence;
		m_Hash = this.computeHash();
	}
	
	public PeakSequence(GenomicSequence sequence, Peak peak, Strand strand){
		super(
				sequence.getRegion(),
				peak.getName(),
				peak.getScore(),
				strand,
				peak.getSignalVal(),
				peak.getPVal(),
				peak.getQVal(),
				peak.getPointSource()
				);
		m_Sequence = sequence;
		m_Hash = this.computeHash();
	}
	
	public PeakSequence(GenomicSequence sequence, String name, int score, Strand strand, double signalVal, double pVal, double qVal, int pointSource){
		super(sequence.getRegion(), name, score, strand, signalVal, pVal, qVal, pointSource);
		m_Sequence = sequence;
		m_Hash = this.computeHash();
	}
	
	private int computeHash(){
		return new HashCodeBuilder(47, 709).append(m_Sequence).append(super.hashCode()).toHashCode();
	}
	
	public GenomicSequence getGenomicSequence(){
		return m_Sequence;
	}
	
	public String getSequence(){
		return m_Sequence.getSequence();
	}
	
	@Override
	public String toString(){
		return m_Sequence.toString() + "\t" + this.getName() + "\t" + this.getScore() + "\t" + this.getStrand() + "\t" + this.getSignalVal() + "\t"
				+ this.getPVal() + "\t" + this.getQVal() + "\t" + this.getPointSource();
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof PeakSequence){
			PeakSequence other = (PeakSequence) o;
			return m_Sequence.equals(other.m_Sequence) && super.equals(o);
		}
		return false;
	}
	
}
