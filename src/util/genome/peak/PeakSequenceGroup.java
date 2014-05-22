package util.genome.peak;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import util.genome.ParsingException;
import util.genome.reader.GenomeReader;

public class PeakSequenceGroup implements Serializable, Iterable<PeakSequence>{
	private static final long serialVersionUID = 1L;
	
	public static final String[][] FORMATS = Parser.PEAK_SEQ_FORMATS;
	
	public static PeakSequenceGroup parsePeakSeqGroup(InputStream s) throws ParsingException{
		return Parser.parsePeakSeqGroup(s);
	}
	
	/**
	 * This method uses the specified GenomeReader to extract the genomic DNA sequence of each peak in the 
	 * peak group and returns a new PeakSequenceGroup containing those peak sequences.
	 * @param reader - genome from which to extract sequences
	 * @param peaks - peaks for which sequences should be extracted
	 * @return a PeakSequenceGroup object containing all the PeakSequence objects generated
	 */
	public static PeakSequenceGroup readFromGenome(GenomeReader reader, Iterable<Peak> peaks){
		return Parser.readFromGenome(reader, peaks);
	}
	
	/**
	 * This method uses the specified GenomeReader to extract the genomic DNA sequence of each peak in the
	 * peak group in a region around the peak summit of size specified by the summitRegion parameter. The
	 * summitRegion parameter specifies how far on either side of the peak summit to look when extracting sequences.
	 * The resulting peak sequences will be of size summitRegion*2 + 1 as a result. The peak summit values will
	 * be used if they are specified by the peak object, otherwise the summit will be considered the center
	 * of the peak region.
	 * @param reader - genome from which to extract sequences
	 * @param peaks - peaks for which sequences should be extracted
	 * @param summitRegion - int specifying how far on either side of the summit to look
	 * @return a PeakSequenceGroup object containing the PeakSequence objects generated
	 */
	public static PeakSequenceGroup readFromGenome(GenomeReader reader, Iterable<Peak> peaks, int summitRegion){
		return Parser.readFromGenome(reader, peaks, summitRegion);
	}
	
	private final List<PeakSequence> m_PeakSeqs = new ArrayList<PeakSequence>();
	private final int m_Hash;
	
	public PeakSequenceGroup(List<PeakSequence> peakSeqs){
		for(PeakSequence ps : peakSeqs){
			m_PeakSeqs.add(ps);
		}
		m_Hash = this.computeHash();
	}
	
	public PeakSequenceGroup(){
		m_Hash = this.computeHash();
	}
	
	private int computeHash(){
		HashCodeBuilder builder = new HashCodeBuilder(701, 353);
		for(PeakSequence ps : this){
			builder.append(ps);
		}
		return builder.toHashCode();
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public String toString(){
		String s = "";
		for(PeakSequence ps : this){
			s += ps + "\n";
		}
		return s;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof PeakSequenceGroup){
			PeakSequenceGroup other = (PeakSequenceGroup) o;
			if(this.size() == other.size()){
				for(int i=0; i<this.size(); i++){
					if(!this.getPeakSequence(i).equals(other.getPeakSequence(i))) return false;
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Iterator<PeakSequence> iterator() {
		return m_PeakSeqs.iterator();
	}
	
	public boolean containse(PeakSequence peakSeq){
		return m_PeakSeqs.contains(peakSeq);
	}
	
	public PeakSequence getPeakSequence(int index){
		return m_PeakSeqs.get(index);
	}
	
	public int size(){
		return m_PeakSeqs.size();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
