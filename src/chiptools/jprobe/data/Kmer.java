package chiptools.jprobe.data;

import util.DNAUtils;
import util.genome.kmer.Kmer.Score;
import jprobe.services.data.AbstractFinalData;

public class Kmer extends AbstractFinalData{
	private static final long serialVersionUID = 1L;
	
	private static final int FWD = 0;
	private static final int RVS = 1;
	private static final int ESCORE = 2;
	private static final int INTENSITY = 3;
	private static final int ZSCORE = 4;
	
	private final util.genome.kmer.Kmer m_Kmer;
	
	public Kmer(util.genome.kmer.Kmer kmer){
		super(5, kmer.size());
		m_Kmer = kmer;

	}
	
	public util.genome.kmer.Kmer getKmer(){
		return m_Kmer;
	}
	
	@Override
	public String toString(){
		String s = "";
		for(int row=0; row<m_Kmer.size(); row++){
			String fwd = m_Kmer.getWord(row);
			String rvs = DNAUtils.reverseCompliment(fwd);
			Score score = m_Kmer.getScore(fwd);
			s += fwd + "\t" + rvs + "\t" + score.ESCORE + "\t" + score.INTENSITY + "\t" + score.ZSCORE;
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
		String fwd = m_Kmer.getWord(rowIndex);
		switch(columnIndex){
		case FWD: return fwd;
		case RVS: return DNAUtils.reverseCompliment(fwd);
		case ESCORE: return m_Kmer.escore(fwd);
		case INTENSITY: return m_Kmer.intensity(fwd);
		case ZSCORE: return m_Kmer.zscore(fwd);
		default: return null;
		}
	}

	@Override
	public void dispose() {
		//do nothing;
	}


}
