package util.genome;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Chromosome implements Comparable<Chromosome>, Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final Comparator<Chromosome> ASCENDING_COMPARATOR = new Comparator<Chromosome>(){

		@Override
		public int compare(Chromosome o1, Chromosome o2) {
			return o1.naturalCompareTo(o2);
		}
		
	};
	
	public static final Comparator<Chromosome> DESCENDING_COMPARATOR = new Comparator<Chromosome>(){

		@Override
		public int compare(Chromosome o1, Chromosome o2) {
			return -o1.naturalCompareTo(o2);
		}
		
	};
	
	public static final String CHR_TAG = "chr";
	public static final String FASTA_CHR_TAG = ">"+CHR_TAG;
	
	private static final Map<String, Chromosome> m_IdMap = new HashMap<String, Chromosome>();
	
	private final String m_Id;
	private final int m_HashCode;
	
	private Chromosome(String id){
		m_Id = id;
		m_HashCode = new HashCodeBuilder(947, 569).append(m_Id).toHashCode();
	}
	
	private static String processId(String id){
		if(id.startsWith(CHR_TAG)){
			id = id.substring(CHR_TAG.length()).trim();
		}else if(id.startsWith(FASTA_CHR_TAG)){
			id = id.substring(FASTA_CHR_TAG.length()).trim();
		}else{
			id = id.trim();
		}
		return id;
	}
	
	public static Chromosome getInstance(String id){
		id = processId(id);
		if(m_IdMap.containsKey(id)) return m_IdMap.get(id);
		Chromosome c = new Chromosome(id);
		m_IdMap.put(id, c);
		return c;
	}
	
	public String getId(){
		return m_Id;
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
		if(o == null){
			return false;
		}
		if(this == o){
			return true;
		}
		if(o instanceof Chromosome){
			Chromosome other = (Chromosome) o;
			return m_Id.equals(other.m_Id);
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
		if(this == o){
			return 0;
		}
		if(o == null){
			return -1;
		}
		return this.naturalCompareTo(o);
	}
	
}
