package chiptools.jprobe.data;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import util.genome.GenomicRegion;

public class AgilentProbe implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final int NUM_ENTRIES = 6;
	public static final int SEQ = 0;
	public static final int CATEGORY = 1;
	public static final int BLANK_BLANK = 2;
	public static final int BLANK = 3;
	public static final int CATEGORY_SECOND = 4;
	public static final int REGION = 5;
	
	public static final String NA_NA = "Na|Na";
	public static final String NA = "Na";
	
	public static String getEntryName(int entry){
		switch(entry){
		case SEQ:
			return "Sequence";
		case CATEGORY:
			return "Category";
		case BLANK_BLANK:
			return "Na";
		case BLANK:
			return "Na";
		case CATEGORY_SECOND:
			return "Category";
		case REGION:
			return "Region";
		default:
			return "";
		}
	}

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
	
	public int getNumEntries(){
		return NUM_ENTRIES;
	}
	
	public String getEntry(int e, int numPlaces){
		switch(e){
		case SEQ:
			return m_Seq;
		case CATEGORY:
			return this.getCategoryAndInfo(numPlaces);
		case BLANK_BLANK:
			return NA_NA;
		case BLANK:
			return NA;
		case CATEGORY_SECOND:
			return this.getCategoryAndInfo(numPlaces);
		case REGION:
			return m_Region.toString();
		default:
			return null;
		}
	}
	
	protected int generateHash(){
		return new HashCodeBuilder(5, 117).append(m_Seq).append(m_Category).append(m_CategoryNum).append(m_Info).append(m_Region).toHashCode();
	}
	
	public String getNaNa(){
		return NA_NA;
	}
	
	public String getNa(){
		return NA;
	}
	
	public String toString(int numPlaces){
		String catAndInfo = this.getCategoryAndInfo(numPlaces);
		return m_Seq + "\t" + catAndInfo + "\t"+NA_NA+"\t"+NA+"\t" + catAndInfo + "\t" + m_Region; 
	}
	
	public String getCategoryAndInfo(int numPlaces){
		String format = "%s_%0" + numPlaces + "d_%s";
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
