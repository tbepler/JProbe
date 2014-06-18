package chiptools.jprobe.function.gcrunmutator;

import java.util.ArrayList;
import java.util.Collection;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.probe.Probe;
import util.progress.ProgressListener;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.PrimerArgument;
import chiptools.jprobe.function.args.ProbesArgument;

public class GCRunMutator extends AbstractChiptoolsFunction<GCRunMutatorParams>{

	protected GCRunMutator() {
		super(GCRunMutatorParams.class);
	}

	@Override
	public Collection<Argument<? super GCRunMutatorParams>> getArguments() {
		Collection<Argument<? super GCRunMutatorParams>> args = new ArrayList<Argument<? super GCRunMutatorParams>>();
		args.add(new ProbesArgument(false));
		args.add(new PrimerArgument(true));
		
		return args;
	}

	@Override
	public Data execute(ProgressListener l, GCRunMutatorParams params) throws Exception {
		
		String primer = params.getPrimer();
		for(Probe p : params.getProbes().getProbeGroup()){
			GenomicRegion region = p.getRegion();
			String seq = p.getSequence();
			
		}
		
		return null;
	}
	
	protected Record mutate(String seq, GenomicRegion region){
		
	}
	
	protected static class Record{
		public final String seq;
		public final Collection<GenomicCoordinate> mutations;
		
		public Record(String seq, Collection<GenomicCoordinate> mutations){
			this.seq = seq; this.mutations = mutations;
		}
	}

}
