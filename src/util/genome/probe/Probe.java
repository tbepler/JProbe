package util.genome.probe;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import util.genome.Chromosome;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.ParsingException;
import util.genome.Strand;

public class Probe implements Serializable, Comparable<Probe>{
	private static final long serialVersionUID = 1L;
	
	public static Probe parseProbe(String s) throws ParsingException{
		try{
			String[] tokens = s.split("\\s");
			String seq = tokens[0];
			String fullName = tokens[2];
			GenomicRegion region = GenomicRegion.parseString(tokens[1]);
			
			//parse full name to name, strand, and mutant
			String[] nameTokens = fullName.split("-");
			if(nameTokens.length == 1){
				return new Probe(seq, region, nameTokens[0]);
			} else if(nameTokens.length == 2){
				String name = nameTokens[0];
				if(nameTokens[1].equals("mutant")){
					return new Probe(seq, region, name, true);
				}else if(nameTokens[1].equals("wildtype")){
					return new Probe(seq, region, name, false);
				}else{
					Strand strand = Strand.parseStrand(nameTokens[1]);
					return new Probe(seq, region, name, strand);
				}
			} else{
				boolean mutant = nameTokens[2].equals("mutant");
				Strand strand = Strand.parseStrand(nameTokens[1]);
				return new Probe(seq, region, nameTokens[0], strand, mutant);
			}
			
		} catch (ParsingException e){
			throw e;
		} catch (Exception e){
			throw new ParsingException("Could not parse Probe from string: "+s);
		}
	}
	
	private static String mutantToString(boolean mutant){
		if(mutant){
			return "-mutant";
		}
		return "-wildtype";
	}
	
	private static String strandToString(Strand s){
		switch(s){
		case PLUS:
			return "-" + s.toString();
		case MINUS:
			return "-" + s.toString();
		default:
			return "";
		}
	}
	
	private final String m_Seq;
	private final GenomicRegion m_Region;
	private final String m_Name;
	private final boolean m_Mutant;
	private final Strand m_Strand;
	private final int m_Hash;
	
	public Probe(String seq){
		this(seq, null, null);
	}
	
	public Probe(String seq, GenomicRegion region){
		this(seq, region, null);
	}
	
	public Probe(String seq, String name){
		this(seq, null, name);
	}

	public Probe(String seq, GenomicRegion region, boolean mutant){
		this(seq, region, null, Strand.UNKNOWN, mutant);
	}
	
	public Probe(String seq, GenomicRegion region, String name){
		this(seq, region, name, Strand.UNKNOWN, false);
	}
	
	public Probe(GenomicSequence seq){
		this(seq, null);
	}
	
	public Probe(GenomicSequence seq, String name){
		this(seq, name, false);
	}
	
	public Probe(GenomicSequence seq, String name, Strand strand){
		this(seq, name, strand, false);
	}
	
	public Probe(GenomicSequence seq, String name, boolean mutant){
		this(seq.getSequence(), seq.getRegion(), name, Strand.UNKNOWN, mutant);
	}
	
	public Probe(GenomicSequence seq, String name, Strand strand, boolean mutant){
		this(seq.getSequence(), seq.getRegion(), name, strand, mutant);
	}
	
	public Probe(String seq, GenomicRegion region, String name, Strand strand){
		this(seq, region, name, strand, false);
	}
	
	public Probe(String seq, GenomicRegion region, String name, boolean mutant){
		this(seq, region, name, Strand.UNKNOWN, mutant);
	}
	
	public Probe(String seq, GenomicRegion region, String name, Strand strand, boolean mutant){
		m_Seq = seq;
		m_Region = region != null ? region : new GenomicRegion(new Chromosome("0"), 1, 1);
		m_Name = name!=null && !name.equals("") ? name : "Probe";
		m_Mutant = mutant;
		m_Strand = strand;
		m_Hash = new HashCodeBuilder(859, 911).append(m_Seq).append(m_Region).toHashCode();
	}
	
	public int getLength(){
		return m_Seq.length();
	}
	
	public GenomicSequence asGenomicSequence(){
		return new GenomicSequence(m_Seq, m_Region);
	}
	
	public boolean isMutant(){
		return m_Mutant;
	}
	
	public String getSequence(){
		return m_Seq;
	}
	
	public GenomicRegion getRegion(){
		return m_Region;
	}
	
	public Strand getStrand(){
		return m_Strand;
	}
	
	public String getName(){
		return m_Name;
	}
	
	public String getFullName(){
		return m_Name + strandToString(m_Strand) + mutantToString(m_Mutant);
	}
	
	@Override
	public String toString(){
		return m_Seq + "\t" + m_Region + "\t" + this.getFullName();
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Probe){
			Probe other = (Probe) o;
			return m_Seq.equals(other.m_Seq) && m_Region.equals(other.m_Region);
		}
		return false;
	}
	
	@Override
	public int compareTo(Probe other){
		if(other == null){
			return -1;
		}
		int comp = m_Region.compareTo(other.m_Region);
		if(comp != 0){
			return comp;
		}
		comp = m_Seq.compareTo(other.m_Seq);
		return comp;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
