package chiptools.jprobe.data;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import jprobe.services.data.AbstractFinalData;

public class Probes extends AbstractFinalData{
	private static final long serialVersionUID = 1L;
	
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
	
	protected String getSeqString(Probe p){
		String s = "<html>";
		GenomicRegion[] bindingSites = p.getBindingSites();
		Set<GenomicSequence> splitSeqs = new TreeSet<GenomicSequence>();
		for(GenomicSequence seq : p.asGenomicSequence().split(bindingSites)){
			splitSeqs.add(seq);
		}
		for(GenomicRegion site : bindingSites){
			splitSeqs.add(p.asGenomicSequence().subsequence(site));
		}
		Set<GenomicSequence> bindingSeqs = new HashSet<GenomicSequence>();
		for(GenomicRegion region : bindingSites){
			bindingSeqs.add(p.asGenomicSequence().subsequence(region));
		}
		for(GenomicSequence seq : splitSeqs){
			if(bindingSeqs.contains(seq)){
				s += "<font color=red>"+seq.getSequence()+"</font>";
			}else{
				s += seq.getSequence();
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
