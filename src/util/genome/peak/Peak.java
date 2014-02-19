package util.genome.peak;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import util.genome.Chromosome;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.ParsingException;
import util.genome.Strand;

public class Peak implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final String[][] FORMATS = Parser.FORMATS;
	
	public static final String NAME = "name";
	public static final String DEFAULT_NAME = ".";
	
	public static final String SCORE = "score";
	public static final int DEFAULT_SCORE = 0;
	
	public static final String STRAND = "strand";
	public static final Strand DEFAULT_STRAND = Strand.UNKNOWN;
	
	public static final String PVAL = "pval";
	public static final double DEFAULT_PVAL = -1;
	
	public static final String QVAL = "qval";
	public static final double DEFAULT_QVAL = -1;
	
	public static final String POINT_SOURCE = "pointsource";
	public static final int DEFAULT_POINT_SOURCE = -1;
	
	public static Peak parsePeak(String s) throws ParsingException{
		return Parser.parsePeak(s);
	}
	
	private GenomicRegion m_Region;
	private String m_Name;
	private int m_Score;
	private Strand m_Strand;
	private double m_SignalVal;
	private double m_PVal;
	private double m_QVal;
	private int m_PointSource;
	
	public Peak(String chrom, long chromStart, long chromEnd, double signalVal){
		this(chrom, chromStart, chromEnd, signalVal, new HashMap<String, Object>());
	}
	
	public Peak(String chrom, long chromStart, long chromEnd, double signalVal, Map<String, Object> optionalParams){
		Chromosome chr = new Chromosome(chrom);
		m_Region = new GenomicRegion(new GenomicCoordinate(chr, chromStart), new GenomicCoordinate(chr, chromEnd));
		m_SignalVal = signalVal;
		m_Name = optionalParams.containsKey(NAME) && optionalParams.get(NAME) instanceof String ? (String)optionalParams.get(NAME) : DEFAULT_NAME;
		m_Score = optionalParams.containsKey(SCORE) && optionalParams.get(SCORE) instanceof Integer ? (Integer)optionalParams.get(SCORE) : DEFAULT_SCORE;
		m_Strand = optionalParams.containsKey(STRAND) && optionalParams.get(STRAND) instanceof Strand ? (Strand)optionalParams.get(STRAND) : DEFAULT_STRAND;
		m_PVal = optionalParams.containsKey(PVAL) && optionalParams.get(PVAL) instanceof Double ? (Double)optionalParams.get(PVAL) : DEFAULT_PVAL;
		m_QVal = optionalParams.containsKey(QVAL) && optionalParams.get(QVAL) instanceof Double ? (Double)optionalParams.get(QVAL) : DEFAULT_QVAL;
		m_PointSource = optionalParams.containsKey(POINT_SOURCE) && optionalParams.get(POINT_SOURCE) instanceof Integer ? 
				(Integer)optionalParams.get(POINT_SOURCE) : DEFAULT_POINT_SOURCE;
	}
	
	public Peak(String chrom, long chromStart, long chromEnd,
			String name, int score, Strand strand, double signalVal,
			double pVal, double qVal, int pointSource) {
		Chromosome chr = new Chromosome(chrom);
		m_Region = new GenomicRegion(new GenomicCoordinate(chr, chromStart), new GenomicCoordinate(chr, chromEnd));
		this.m_Name = name;
		this.m_Score = score;
		this.m_Strand = strand;
		this.m_SignalVal = signalVal;
		this.m_PVal = pVal;
		this.m_QVal = qVal;
		this.m_PointSource = pointSource;
	}
	
	@Override
	public String toString(){
		return this.getChrom() + "\t" + this.getChromStart() + "\t" + this.getChromEnd() + "\t" +m_Name + "\t" +m_Score + "\t" +m_Strand + "\t" +m_SignalVal
				+ "\t" + m_PVal + "\t" + m_QVal + "\t" + m_PointSource;
	}
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder(283, 977).append(m_Region).append(m_Name).append(m_Score).append(m_Strand)
				.append(m_SignalVal).append(m_PVal).append(m_QVal).append(m_PointSource).toHashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Peak){
			Peak other = (Peak) o;
			return m_Region.equals(other.m_Region)
					&& m_Name.equals(other.m_Name) && m_Score == other.m_Score && m_Strand == other.m_Strand
					&& m_SignalVal == other.m_SignalVal && m_PVal == other.m_PVal && m_QVal == other.m_QVal
					&& m_PointSource == other.m_PointSource;
		}
		return false;
	}
	
	public GenomicRegion getRegion(){
		return m_Region;
	}
	
	public GenomicCoordinate getStart(){
		return m_Region.getStart();
	}
	
	public GenomicCoordinate getEnd(){
		return m_Region.getEnd();
	}
	
	public Chromosome getChrom() {
		return m_Region.getStart().getChromosome();
	}
	
	public long getChromStart() {
		return this.getStart().getBaseIndex();
	}
	
	public long getChromEnd() {
		return this.getEnd().getBaseIndex();
	}
	
	public String getName() {
		return m_Name;
	}
	public int getScore() {
		return m_Score;
	}
	public Strand getStrand() {
		return m_Strand;
	}
	public double getSignalVal() {
		return m_SignalVal;
	}
	public double getPVal() {
		return m_PVal;
	}
	public double getQVal() {
		return m_QVal;
	}
	public int getPointSource() {
		return m_PointSource;
	}
	
}
