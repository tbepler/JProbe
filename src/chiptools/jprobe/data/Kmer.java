package chiptools.jprobe.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

import chiptools.jprobe.field.*;
import util.DNAUtils;
import jprobe.services.data.Data;
import jprobe.services.data.DataListener;
import jprobe.services.data.Field;

public class Kmer implements Data{
	private static final long serialVersionUID = 1L;
	
	private class Row implements Comparable<Row>{
		
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
	
	private final util.genome.kmer.Kmer m_Kmer;
	private final Field[][] m_Table;
	
	public Kmer(util.genome.kmer.Kmer kmer){
		m_Kmer = kmer;
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
		m_Table = new Field[rows.size()][5];
		int i = 0;
		while(!rows.isEmpty()){
			Row cur = rows.poll();
			m_Table[i][0] = new StringField(cur.fwd, "Forward motif");
			m_Table[i][1] = new StringField(cur.rvs, "Reverse motif");
			m_Table[i][2] = new DecimalField(cur.escore, "EScore");
			m_Table[i][3] = new DecimalField(cur.intensity, "Intensity");
			m_Table[i][4] = new DecimalField(cur.zscore, "ZScore");
			i++;
		}

	}
	
	public util.genome.kmer.Kmer getKmer(){
		return m_Kmer;
	}

	@Override
	public void addDataListener(DataListener listener) {
		//do nothing
	}

	@Override
	public void removeDataListener(DataListener listener) {
		//do nothing
	}

	@Override
	public boolean isModifiable(int row, int col) {
		//structure is final
		return false;
	}

	@Override
	public String[] getHeaders() {
		return new String[]{"Forward", "Reverse", "EScore", "Intensity", "ZScore"};
	}

	@Override
	public Field[][] toTable() {
		return m_Table;
	}
	
	@Override
	public String toString(){
		String s = "";
		for(int row=0; row<m_Table.length; row++){
			for(int col=0; col<m_Table[row].length; col++){
				s += m_Table[row][col] + "\t";
			}
			s+="\n";
		}
		return s;
	}

	@Override
	public boolean setValue(int row, int col, Field value) {
		//final
		return false;
	}

	@Override
	public Field getValue(int row, int col) {
		return m_Table[row][col];
	}

	@Override
	public int getNumRows() {
		return m_Table.length;
	}

	@Override
	public int getNumCols() {
		return 5;
	}

	@Override
	public String getTooltip() {
		return "This data structure contains kmer data.";
	}

}
