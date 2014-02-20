package chiptools.jprobe.data;

import chiptools.Constants;
import chiptools.jprobe.field.*;
import util.genome.peak.PeakSequence;
import util.genome.peak.PeakSequenceGroup;
import jprobe.services.data.Data;
import jprobe.services.data.DataListener;
import jprobe.services.data.Field;

public class PeakSequences implements Data{
	private static final long serialVersionUID = 1L;

	private final PeakSequenceGroup m_PeakSeqs;
	private final Field[][] m_Table;
	
	public PeakSequences(PeakSequenceGroup peakSeqs){
		m_PeakSeqs = peakSeqs;
		m_Table = new Field[m_PeakSeqs.size()][];
		for(int i=0; i<m_Table.length; i++){
			m_Table[i] = this.generatePeakSeqFields(m_PeakSeqs.getPeakSequence(i));
		}
	}
	
	public PeakSequenceGroup getPeakSeqs(){
		return m_PeakSeqs;
	}
	
	@Override
	public String toString(){
		return m_PeakSeqs.toString();
	}
	
	protected Field[] generatePeakSeqFields(PeakSequence peakSeq){
		Field[] fields = new Field[PeakSequence.ELEMENTS];
		fields[0] = new StringField(peakSeq.getSequence());
		fields[1] = new GenomicRegionField(peakSeq.getRegion());
		fields[2] = new StringField(peakSeq.getName());
		fields[3] = new UCSCScoreField((short) peakSeq.getScore());
		fields[4] = new StrandField(peakSeq.getStrand());
		fields[5] = new SignalValueField(peakSeq.getSignalVal());
		fields[6] = new PValueField(peakSeq.getPVal());
		fields[7] = new QValueField(peakSeq.getQVal());
		fields[8] = new PointSourceField(peakSeq.getPointSource(), peakSeq.getRegion());
		return fields;
	}
	
	@Override
	public void addDataListener(DataListener listener) {
		//do nothing this is final
	}

	@Override
	public void removeDataListener(DataListener listener) {
		//final
	}

	@Override
	public boolean isModifiable(int row, int col) {
		//final
		return false;
	}

	@Override
	public String[] getHeaders() {
		return Constants.PEAK_SEQ_HEADER;
	}

	@Override
	public Field[][] toTable() {
		return m_Table;
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
		return Constants.NUM_PEAK_SEQ_FIELDS;
	}

	@Override
	public String getTooltip() {
		return Constants.PEAK_SEQ_TOOLTIP;
	}

}
