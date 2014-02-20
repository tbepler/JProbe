package chiptools.jprobe.data;

import chiptools.Constants;
import chiptools.jprobe.field.*;
import util.genome.peak.Peak;
import util.genome.peak.PeakGroup;
import jprobe.services.data.Data;
import jprobe.services.data.DataListener;
import jprobe.services.data.Field;

public class Peaks implements Data{
	private static final long serialVersionUID = 1L;
	
	private final PeakGroup m_Peaks;
	private final Field[][] m_Table;
	
	public Peaks(PeakGroup peaks){
		m_Peaks = peaks;
		m_Table = new Field[m_Peaks.size()][];
		for(int i=0; i<m_Table.length; i++){
			m_Table[i] = this.generatePeakFields(m_Peaks.getPeak(i));
		}
	}
	
	@Override
	public String toString(){
		return m_Peaks.toString();
	}
	
	protected Field[] generatePeakFields(Peak p){
		Field[] fields = new Field[Constants.NUM_PEAK_FIELDS];
		fields[0] = new ChromosomeField(p.getChrom());
		fields[1] = new ChromosomeBaseField(p.getChrom(), p.getChromStart());
		fields[2] = new ChromosomeBaseField(p.getChrom(), p.getChromEnd());
		fields[3] = new StringField(p.getName());
		fields[4] = new UCSCScoreField((short) p.getScore());
		fields[5] = new StrandField(p.getStrand());
		fields[6] = new SignalValueField(p.getSignalVal());
		fields[7] = new PValueField(p.getPVal());
		fields[8] = new QValueField(p.getQVal());
		fields[9] = new PointSourceField(p.getPointSource(), p.getRegion());
		return fields;
	}

	@Override
	public void addDataListener(DataListener listener) {
		//do nothing, this data type is final
	}

	@Override
	public void removeDataListener(DataListener listener) {
		//do nothing, this data type is final
	}

	@Override
	public boolean isModifiable(int row, int col) {
		//always false, data is final
		return false;
	}

	@Override
	public Field[][] toTable() {
		return m_Table;
	}

	@Override
	public boolean setValue(int row, int col, Field value) {
		//this is final
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
		return Constants.NUM_PEAK_FIELDS;
	}

	@Override
	public String getTooltip() {
		return Constants.PEAKS_TOOLTIP;
	}

	@Override
	public String[] getHeaders() {
		return Constants.PEAK_HEADER;
	}

}
