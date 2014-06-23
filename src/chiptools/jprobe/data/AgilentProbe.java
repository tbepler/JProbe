package chiptools.jprobe.data;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import util.genome.GenomicRegion;

public class AgilentProbe implements Serializable{
	private static final long serialVersionUID = 1L;

	private final String m_Seq;
	private final String m_Category;
	private final int m_CategoryNum;
	private final String m_Info;
	private final GenomicRegion m_Region;
	private final int m_Hash;
	
	public AgilentProbe(String seq, String category, int categoryNum, String info, GenomicRegion region){
		m_Seq = seq;
		m_Category = category;
		m_CategoryNum = categoryNum;
		m_Info = info;
		m_Region = region;
		m_Hash = this.generateHash();
	}
	
	protected int generateHash(){
		return new HashCodeBuilder(5, 117).append(m_Seq).append(m_Category).append(m_CategoryNum).append(m_Info).append(m_Region).toHashCode();
	}
	
	public String toString(int numPlaces){
		String catAndInfo = this.getCategoryAndInfo(numPlaces);
		return m_Seq + "\t" + catAndInfo + "\tNa|Na\tNa\t" + catAndInfo + "\t" + m_Region; 
	}
	
	public String getCategoryAndInfo(int numPlaces){
		String format = "%s_%-0" + numPlaces + "s_%s";
		return String.format( format , m_Category, m_CategoryNum, m_Info );
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof AgilentProbe){
			AgilentProbe ap = (AgilentProbe) o;
			return m_Seq.equals(ap.m_Seq)
					&& m_Category.equals(ap.m_Category)
					&& m_CategoryNum == ap.m_CategoryNum
					&& m_Info.equals(ap.m_Info)
					&& m_Region.equals(ap.m_Region);
		}
		return false;
	}
	
}
