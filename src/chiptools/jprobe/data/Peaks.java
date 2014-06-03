package chiptools.jprobe.data;

import util.genome.peak.Peak;
import util.genome.peak.PeakGroup;
import jprobe.services.data.AbstractFinalData;

public class Peaks extends AbstractFinalData{
	private static final long serialVersionUID = 1L;
	
	private static final int PEAK_COLS = 10;
	
	private static final int CHROM = 0;
	private static final int CHROM_START = 1;
	private static final int CHROM_END = 2;
	private static final int NAME = 3;
	private static final int UCSC_SCORE = 4;
	private static final int STRAND = 5;
	private static final int SIGNAL_VAL = 6;
	private static final int PVAL = 7;
	private static final int QVAL = 8;
	private static final int POINT_SOURCE = 9;
	
	private final PeakGroup m_Peaks;
	
	public Peaks(PeakGroup peaks){
		super(PEAK_COLS , peaks.size());
		m_Peaks = peaks;
		
	}
	
	public PeakGroup getPeaks(){
		return m_Peaks;
	}
	
	@Override
	public String toString(){
		return m_Peaks.toString();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex){
		case CHROM: return String.class;
		case CHROM_START: return Long.class;
		case CHROM_END: return Long.class;
		case NAME: return String.class;
		case UCSC_SCORE: return Integer.class;
		case STRAND: return String.class;
		case SIGNAL_VAL: return Double.class;
		case PVAL: return Double.class;
		case QVAL: return Double.class;
		case POINT_SOURCE: return Integer.class;
		default: return null;
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex){
		case CHROM: return "Chromosome";
		case CHROM_START: return "Start";
		case CHROM_END: return "End";
		case NAME: return "Name";
		case UCSC_SCORE: return "Score";
		case STRAND: return "Strand";
		case SIGNAL_VAL: return "Signal value";
		case PVAL: return "P-value";
		case QVAL: return "Q-value";
		case POINT_SOURCE: return "Point source";
		default: return null;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Peak p = m_Peaks.getPeak(rowIndex);
		switch(columnIndex){
		case CHROM: return p.getChrom().toString();
		case CHROM_START: return p.getChromStart();
		case CHROM_END: return p.getChromEnd();
		case NAME: return p.getName();
		case UCSC_SCORE: return p.getScore();
		case STRAND: return p.getStrand().toString();
		case SIGNAL_VAL: return p.getSignalVal();
		case PVAL: return p.getPVal();
		case QVAL: return p.getQVal();
		case POINT_SOURCE: return p.getPointSource();
		default: return null;
		}
	}

	@Override
	public void dispose() {
		//do nothing
	}

}
