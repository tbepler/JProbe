package chiptools.jprobe.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import util.DNAUtils;
import jprobe.services.data.AbstractFinalData;

public class Kmer extends AbstractFinalData{
	private static final long serialVersionUID = 1L;
	
	private static class Row implements Comparable<Row>, Serializable{
		private static final long serialVersionUID = 1L;
		
		public final String fwd;
		public final String rvs;
		public final double escore;
		public final double intensity;
		public final double zscore;
		
		public Row(String fwd, String rvs, double escore, double intensity, double zscore){
			this.fwd = fwd;
			this.rvs = rvs;
			this.escore = escore;
			this.intensity = intensity;
			this.zscore = zscore;
		}
		
		@Override
		public int compareTo(Row o) {
			if(o.escore > this.escore){
				return 1;
			}
			if(o.escore < this.escore){
				return -1;
			}
			return 0;
		}
		
	}
	
	private static List<Row> getRows(util.genome.kmer.Kmer kmer){
		Collection<String> entered = new HashSet<String>();
		Queue<Row> rows = new PriorityQueue<Row>();
		for(String word : kmer){
			if(entered.contains(word)) continue;
			String revcomp = DNAUtils.reverseCompliment(word);
			entered.add(word);
			entered.add(revcomp);
			double escore = kmer.escore(word);
			double intensity = kmer.intensity(word);
			double zscore = kmer.zscore(word);
			rows.add(new Row(word, revcomp, escore, intensity, zscore));
		}
		return new ArrayList<Row>(rows);
	}
	
	private static final int FWD = 0;
	private static final int RVS = 1;
	private static final int ESCORE = 2;
	private static final int INTENSITY = 3;
	private static final int ZSCORE = 4;
	
	private final util.genome.kmer.Kmer m_Kmer;
	private final List<Row> m_Rows;
	
	public Kmer(util.genome.kmer.Kmer kmer){
		super(5, getRows(kmer).size());
		m_Kmer = kmer;
		m_Rows = getRows(kmer);

	}
	
	public util.genome.kmer.Kmer getKmer(){
		return m_Kmer;
	}
	
	@Override
	public String toString(){
		String s = "";
		for(int row=0; row<m_Rows.size(); row++){
			Row r = m_Rows.get(row);
			s += r.fwd + "\t" + r.rvs + "\t" + r.escore + "\t" + r.intensity + "\t" + r.zscore;
			s += "\n";
		}
		return s;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex){
		case FWD: return String.class;
		case RVS: return String.class;
		case ESCORE: return Double.class;
		case INTENSITY: return Double.class;
		case ZSCORE: return Double.class;
		default: return null;
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex){
		case FWD: return "Forward";
		case RVS: return "Reverse";
		case ESCORE: return "E-score";
		case INTENSITY: return "Intensity";
		case ZSCORE: return "Z-score";
		default: return null;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Row r = m_Rows.get(rowIndex);
		switch(columnIndex){
		case FWD: return r.fwd;
		case RVS: return r.rvs;
		case ESCORE: return r.escore;
		case INTENSITY: return r.intensity;
		case ZSCORE: return r.zscore;
		default: return null;
		}
	}


}
