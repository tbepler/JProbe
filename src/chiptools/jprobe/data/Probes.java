package chiptools.jprobe.data;

import chiptools.jprobe.field.*;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import jprobe.services.data.Data;
import jprobe.services.data.DataListener;
import jprobe.services.data.Field;

public class Probes implements Data{
	private static final long serialVersionUID = 1L;
	
	private final ProbeGroup m_Probes;
	private final Field[][] m_Table;
	
	public Probes(ProbeGroup probes){
		m_Probes = probes;
		m_Table = new Field[probes.size()][3];
		for(int row=0; row<m_Table.length; row++){
			Probe p = probes.getProbe(row);
			m_Table[row][0] = new StringField(p.getSequence(), "Sequence");
			m_Table[row][1] = new GenomicRegionField(p.getRegion());
			m_Table[row][2] = new StringField(p.getFullName(), "Name");
		}
	}
	
	@Override
	public String toString(){
		return m_Probes.toString();
	}
	
	public ProbeGroup getProbeGroup(){
		return m_Probes;
	}
	
	@Override
	public void addDataListener(DataListener listener) {
		//final
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
		return new String[]{"Sequence", "Region", "Name"};
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
		return 3;
	}

	@Override
	public String getTooltip() {
		return "This data structure represents a probe group.";
	}

}
