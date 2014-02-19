package util.genome;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Chromosome implements Comparable<Chromosome>, Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final String CHR_TAG = "chr";
	public static final String FASTA_CHR_TAG = ">"+CHR_TAG;
	
	private final String m_Id;
	private final long m_Size;
	private final GenomicContext m_Context;
	private final int m_HashCode;
	
	public Chromosome(String id){
		this(id, -1);
	}
	
	public Chromosome(String id, long size){
		this(null, id, size);
	}
	
	public Chromosome(GenomicContext context, String id, long size){
		m_Context = context;
		if(id.startsWith(CHR_TAG)){
			m_Id = id.substring(CHR_TAG.length()).trim();
		}else if(id.startsWith(FASTA_CHR_TAG)){
			m_Id = id.substring(FASTA_CHR_TAG.length()).trim();
		}else{
			m_Id = id.trim();
		}
		m_Size = size;
		m_HashCode = new HashCodeBuilder(947, 569).append(m_Id).append(m_Size).append(m_Context).toHashCode();
	}
	
	public GenomicContext getGenomicContext(){
		return m_Context;
	}
	
	public boolean hasReferenceGenome(){
		return m_Context != null;
	}
	
	public Chromosome nextChr(){
		if(m_Context == null){
			return null;
		}
		return m_Context.nextChr(this);
	}
	
	public Chromosome prevChr(){
		if(m_Context == null){
			return null;
		}
		return m_Context.prevChr(this);
	}
	
	public String getId(){
		return m_Id;
	}
	
	public long getSize(){
		return m_Size;
	}
	
	@Override
	public String toString(){
		return CHR_TAG + m_Id;
	}
	
	@Override
	public int hashCode(){
		return m_HashCode;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof Chromosome){
			Chromosome other = (Chromosome) o;
			return m_Id.equals(other.m_Id) && m_Size == other.m_Size && m_Context == other.m_Context;
		}
		return false;
	}
	
	protected int naturalCompareTo(Chromosome o){
		try{
			int chr = Integer.parseInt(m_Id);
			try{
				int oChr = Integer.parseInt(o.m_Id);
				return chr - oChr;
			} catch (Exception e){
				return -1;
			}
		} catch (Exception e){
			try{
				Integer.parseInt(o.m_Id);
				return 1;
			} catch (Exception e1){
				return m_Id.compareTo(o.m_Id);
			}
		}
	}

	@Override
	public int compareTo(Chromosome o) {
		if(o == null) return -1;
		if(m_Context == null){
			return this.naturalCompareTo(o);
		}
		Comparator<Chromosome> comparator = m_Context.getChrAscendingComparator();
		return comparator.compare(this, o);
	}
	
}
