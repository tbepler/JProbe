package chiptools.jprobe.data;

import util.genome.peak.PeakSequence;
import util.genome.peak.PeakSequenceGroup;
import jprobe.services.data.AbstractFinalData;

public class PeakSequences extends AbstractFinalData{
	private static final long serialVersionUID = 1L;
	
	private static final int PEAKSEQ_COLS = 9;
	
	private static final int SEQ = 0;
	private static final int REGION = 1;
	private static final int NAME = 2;
	private static final int SCORE = 3;
	private static final int STRAND = 4;
	private static final int SIGNAL_VAL = 5;
	private static final int PVAL = 6;
	private static final int QVAL = 7;
	private static final int POINT_SOURCE = 8;
	
	private final PeakSequenceGroup m_PeakSeqs;
	
	public PeakSequences(PeakSequenceGroup peakSeqs){
		super(PEAKSEQ_COLS, peakSeqs.size());
		m_PeakSeqs = peakSeqs;
	}
	
	public PeakSequenceGroup getPeakSeqs(){
		return m_PeakSeqs;
	}
	
	@Override
	public String toString(){
		return m_PeakSeqs.toString();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex){
		case SEQ: return String.class;
		case REGION: return String.class;
		case NAME: return String.class;
		case SCORE: return Integer.class;
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
		case SEQ: return "Sequence";
		case REGION: return "Region";
		case NAME: return "Name";
		case SCORE: return "Score";
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
		PeakSequence p = m_PeakSeqs.getPeakSequence(rowIndex);
		switch(columnIndex){
		case SEQ: return p.getSequence();
		case REGION: return p.getRegion().toString();
		case NAME: return p.getName();
		case SCORE: return p.getScore();
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
