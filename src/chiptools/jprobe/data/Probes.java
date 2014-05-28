package chiptools.jprobe.data;

import java.util.Collection;
import java.util.HashSet;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import jprobe.services.data.AbstractFinalData;

public class Probes extends AbstractFinalData{
	private static final long serialVersionUID = 1L;
	
	private static final String BINDING_SITE_HTML_START = "<font color=red>";
	private static final String BINDING_SITE_HTML_END ="</font>";
	private static final String MUT_SITE_HTML_START = "<font color=blue>";
	private static final String MUT_SITE_HTML_END = "</font>";
	
	private static final int PROBE_COLS = 6;
	
	private static final int SEQ = 0;
	private static final int REGION = 1;
	private static final int NAME = 2;
	private static final int STRAND = 3;
	private static final int MUT = 4;
	private static final int BINDING = 5;
	
	private final ProbeGroup m_Probes;
	
	public Probes(ProbeGroup probes){
		super(PROBE_COLS, probes.size());
		m_Probes = probes;
	}
	
	private String addMutCoord(GenomicCoordinate coord, GenomicSequence seq, Collection<GenomicCoordinate> mutations){
		String s = "";
		if(coord.equals(seq.getStart()) || !mutations.contains(coord.decrement(1))){
			s += MUT_SITE_HTML_START;
		}
		s += seq.getBaseAt(coord);
		if(coord.equals(seq.getEnd()) || !mutations.contains(coord.increment(1))){
			s += MUT_SITE_HTML_END;
		}
		return s;
	}
	
	private String addBindingCoord(GenomicCoordinate coord, GenomicSequence seq, Collection<GenomicCoordinate> mutations, Collection<GenomicCoordinate> binding){
		String s = "";
		if(coord.equals(seq.getStart()) || mutations.contains(coord.decrement(1)) || !binding.contains(coord.decrement(1))){
			s += BINDING_SITE_HTML_START;
		}
		s += seq.getBaseAt(coord);
		if(coord.equals(seq.getEnd()) || mutations.contains(coord.increment(1)) || !binding.contains(coord.increment(1))){
			s += BINDING_SITE_HTML_END;
		}
		return s;
	}
	
	protected String getSeqString(Probe p){
		String s = "<html>";
		GenomicSequence seq = p.asGenomicSequence();
		Collection<GenomicCoordinate> bindingSites = new HashSet<GenomicCoordinate>();
		for(GenomicRegion r : p.getBindingSites()){
			for(GenomicCoordinate c : r){
				bindingSites.add(c);
			}
		}
		Collection<GenomicCoordinate> mutations = p.getMutations();
		
		for(GenomicCoordinate coord : seq){
			if(mutations.contains(coord)){
				s += this.addMutCoord(coord, seq, mutations);
			}else if(bindingSites.contains(coord)){
				s += this.addBindingCoord(coord, seq, mutations, bindingSites);
			}else{
				s += seq.getBaseAt(coord);
			}
		}
		s += "</html>";
		return s;
	}
	
	@Override
	public String toString(){
		return m_Probes.toString();
	}
	
	public ProbeGroup getProbeGroup(){
		return m_Probes;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex){
		case SEQ: return String.class;
		case REGION: return String.class;
		case NAME: return String.class;
		case STRAND: return String.class;
		case MUT: return String.class;
		case BINDING: return String.class;
		default: return null;
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex){
		case SEQ: return "Sequence";
		case REGION: return "Region";
		case NAME: return "Name";
		case STRAND: return "Strand";
		case MUT: return "Mutation";
		case BINDING: return "Binding sites";
		default: return null;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Probe p = m_Probes.getProbe(rowIndex);
		switch(columnIndex){
		case SEQ: return this.getSeqString(p);
		case REGION: return p.getRegion().toString();
		case NAME: return p.getName();
		case STRAND: return p.getStrand().toString();
		case MUT: return p.getMutantAsString();
		case BINDING: return p.getBindingSitesAsString();
		default: return null;
		}
	}
	
	

}
