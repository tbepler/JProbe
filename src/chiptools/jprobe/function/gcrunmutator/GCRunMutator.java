package chiptools.jprobe.function.gcrunmutator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.DNAUtils;
import util.genome.GenomicCoordinate;
import util.genome.GenomicSequence;
import util.genome.Strand;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.progress.ProgressListener;
import chiptools.Constants;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.PrimerArgument;
import chiptools.jprobe.function.args.ProbesArgument;

public class GCRunMutator extends AbstractChiptoolsFunction<GCRunMutatorParams>{
	
	public GCRunMutator() {
		super(GCRunMutatorParams.class);
	}

	@Override
	public Collection<Argument<? super GCRunMutatorParams>> getArguments() {
		Collection<Argument<? super GCRunMutatorParams>> args = new ArrayList<Argument<? super GCRunMutatorParams>>();
		args.add(new ProbesArgument(this, false));
		args.add(new PrimerArgument(this, true));
		
		return args;
	}

	@Override
	public Data execute(ProgressListener l, GCRunMutatorParams params) throws Exception {
		
		List<Probe> probes = new ArrayList<Probe>();
		String primer = params.getPrimer();
		String rvsPrimer = primer == null ? null : DNAUtils.reverseCompliment(primer);
		for(Probe p : params.getProbes().getProbeGroup()){
			Set<Mutation> mutations = new HashSet<Mutation>();
			GenomicSequence seq = primer == null ? p.asGenomicSequence() : p.asGenomicSequence().appendSuffix(primer);
			seq = this.mutate(seq, mutations, p.getStrand());
			if(primer != null){
				seq = seq.subsequence(seq.getStart(), seq.getEnd().decrement(primer.length()));
			}
			if(rvsPrimer != null){
				seq = p.asGenomicSequence().appendPrefix(rvsPrimer);
				seq = this.mutate(seq, mutations, p.getStrand());
				seq = seq.subsequence(seq.getStart().increment(primer.length()));
			}
			
			Set<GenomicCoordinate> probeMuts = new HashSet<GenomicCoordinate>(p.getMutations());
			boolean mut = false;
			for(Mutation m : mutations){
				if(seq.contains(m.coord)){
					probeMuts.add(m.coord);
					mut = true;
				}
			}
			if(mut){
				probes.add(new Probe(p, seq, new ArrayList<GenomicCoordinate>(probeMuts), probeMuts.isEmpty()));
			}else{
				probes.add(p);
			}
		}
		
		return new Probes(new ProbeGroup(probes));
	}
	
	protected GenomicSequence mutate(GenomicSequence seq, Collection<Mutation> mutations, Strand strand){
		int run = Constants.GRUN.length();
		for(int i=0; i<seq.length()-run; i++){
			if(seq.getSequence().regionMatches(true, i, Constants.GRUN, 0, run)){
				GenomicCoordinate coord = seq.toCoordinate(i+run/2, strand);
				mutations.add(new Mutation(coord, 'C'));
				seq = seq.mutate(coord, 'C');
			}else if(seq.getSequence().regionMatches(true, i, Constants.CRUN, 0, run)){
				GenomicCoordinate coord = seq.toCoordinate(i+run/2, strand);
				mutations.add(new Mutation(coord, 'G'));
				seq = seq.mutate(coord, 'G');
			}
		}
		return seq;
	}
	
	protected static class Mutation{
		public final GenomicCoordinate coord;
		public final char base;
		private final int m_Hash;
		public Mutation(GenomicCoordinate coord, char base){
			this.coord = coord; this.base = base;
			m_Hash = new HashCodeBuilder(5, 11).append(coord).append(base).toHashCode();
		}
		@Override
		public boolean equals(Object o){
			if(o == null) return false;
			if(o == this) return true;
			if(o instanceof Mutation){
				Mutation m = (Mutation) o;
				return coord.equals(m.coord) && base == m.base;
			}
			return false;
		}
		@Override
		public int hashCode(){
			return m_Hash;
		}
	}

}
