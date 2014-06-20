package chiptools.jprobe.data;

import java.util.Collection;
import java.util.HashSet;

import javax.swing.event.TableModelEvent;

import chiptools.jprobe.Preferences;
import chiptools.jprobe.Preferences.Update;
import util.Observer;
import util.Subject;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.Strand;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import jprobe.services.data.AbstractFinalData;

public class Probes extends AbstractFinalData implements Observer<Preferences.Update>{
	private static final long serialVersionUID = 1L;
	
	private static final int PROBE_COLS = 7;
	
	private static final int SEQ = 0;
	private static final int REGION = 1;
	private static final int NAME = 2;
	private static final int STRAND = 3;
	private static final int MUT = 4;
	private static final int BINDING = 5;
	private static final int MUTATIONS = 6;
	
	private final ProbeGroup m_Probes;
	
	public Probes(ProbeGroup probes){
		super(PROBE_COLS, probes.size());
		m_Probes = probes;
		Preferences.getInstance().register(this);
	}
	
	private String stringMutCoord(GenomicCoordinate coord, GenomicSequence seq, Collection<GenomicCoordinate> mutations){
		String s = "";
		if(coord.equals(seq.getStart()) || !mutations.contains(coord.decrement(1))){
			s += Preferences.getInstance().getMutStartHTML();
		}
		s += seq.getBaseAt(coord);
		if(coord.equals(seq.getEnd()) || !mutations.contains(coord.increment(1))){
			s += Preferences.getInstance().getMutEndHTML();
		}
		return s;
	}
	
	/*
	 * On the MINUS strand, the string actually starts with the last coordinate and ends with the first coordinate.
	 * The mutation and binding site coordinates, however, are in accordance with the PLUS strand. Therefore,
	 * the html needs to start when the END of a binding site or mutation site is reached and the html then
	 * needs to end at the START of said binding site or mutation site.
	 */
	private String stringMutCoordMinusStrand(GenomicCoordinate coord, GenomicSequence seq, Collection<GenomicCoordinate> mutations){
		String s = "";
		if(coord.equals(seq.getEnd()) || !mutations.contains(coord.increment(1))){
			s += Preferences.getInstance().getMutStartHTML();
		}
		s += seq.getBaseAt(coord, Strand.MINUS);
		if(coord.equals(seq.getStart()) || !mutations.contains(coord.decrement(1))){
			s += Preferences.getInstance().getMutEndHTML();
		}
		return s;
	}
	
	private String stringBindingCoord(GenomicCoordinate coord, GenomicSequence seq, Collection<GenomicCoordinate> mutations, Collection<GenomicCoordinate> binding){
		String s = "";
		if(coord.equals(seq.getStart()) || mutations.contains(coord.decrement(1)) || !binding.contains(coord.decrement(1))){
			s += Preferences.getInstance().getBindingStartHTML();
		}
		s += seq.getBaseAt(coord);
		if(coord.equals(seq.getEnd()) || mutations.contains(coord.increment(1)) || !binding.contains(coord.increment(1))){
			s += Preferences.getInstance().getBindingEndHTML();
		}
		return s;
	}
	
	/*
	 * On the MINUS strand, the string actually starts with the last coordinate and ends with the first coordinate.
	 * The mutation and binding site coordinates, however, are in accordance with the PLUS strand. Therefore,
	 * the html needs to start when the END of a binding site or mutation site is reached and the html then
	 * needs to end at the START of said binding site or mutation site.
	 */
	private String stringBindingCoordMinusStrand(GenomicCoordinate coord, GenomicSequence seq, Collection<GenomicCoordinate> mutations, Collection<GenomicCoordinate> binding){
		String s = "";
		if(coord.equals(seq.getEnd()) || mutations.contains(coord.increment(1)) || !binding.contains(coord.increment(1))){
			s += Preferences.getInstance().getBindingStartHTML();
		}
		s += seq.getBaseAt(coord, Strand.MINUS);
		if(coord.equals(seq.getStart()) || mutations.contains(coord.decrement(1)) || !binding.contains(coord.decrement(1))){
			s += Preferences.getInstance().getBindingEndHTML();
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
		
		if(p.getStrand() != Strand.MINUS){
			//This strand is not minus, so it is either plus or unknown. If unknown, this probe
			//is assumed to be plus
			for(GenomicCoordinate coord : seq){
				if(mutations.contains(coord)){
					s += this.stringMutCoord(coord, seq, mutations);
				}else if(bindingSites.contains(coord)){
					s += this.stringBindingCoord(coord, seq, mutations, bindingSites);
				}else{
					s += seq.getBaseAt(coord);
				}
			}
		}else{
			//on the minus strand, so build string from last coordinate to first coordinate
			//it is only necessary to reverse the coordinate when accessing the character,
			//because coordinates are always for the plus strand.
			//
			//it is also important to start the html at the end of binding/mutation sites
			//and end the html at the start of binding/mutation sites becuase of this.
			for(GenomicCoordinate coord = seq.getEnd(); coord.compareTo(seq.getStart()) >= 0; coord = coord.decrement(1)){
				if(mutations.contains(coord)){
					s += this.stringMutCoordMinusStrand(coord, seq, mutations);
				}else if(bindingSites.contains(coord)){
					s += this.stringBindingCoordMinusStrand(coord, seq, mutations, bindingSites);
				}else{
					s += seq.getBaseAt(coord, Strand.MINUS);
				}
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
		case MUTATIONS: return String.class;
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
		case MUT: return "Mutated";
		case BINDING: return "Binding sites";
		case MUTATIONS: return "Mutations";
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
		case MUTATIONS: return p.getMutationsAsString();
		default: return null;
		}
	}

	@Override
	public void update(Subject<Update> observed, Update notification) {
		if(observed == Preferences.getInstance()){
			this.notifyListeners(new TableModelEvent(this, 0, this.getRowCount()-1, 0));
		}
	}

	@Override
	public void dispose() {
		Preferences.getInstance().unregister(this);
	}
	
	

}
