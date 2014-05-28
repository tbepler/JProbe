package chiptools.jprobe.function.probemutator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.genome.probe.ProbeUtils;
import util.progress.ProgressListener;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.*;

public class ProbeMutator extends AbstractChiptoolsFunction<ProbeMutatorParams>{
	
	private static final Collection<Character> DNA_ALPHABET = generateAlphabet();
			
	private static Collection<Character> generateAlphabet(){
		Collection<Character> alph = new HashSet<Character>();
		alph.add('A');
		alph.add('C');
		alph.add('G');
		alph.add('T');
		return Collections.unmodifiableCollection(alph);
	}

	public ProbeMutator() {
		super(ProbeMutatorParams.class);
	}

	@Override
	public Collection<Argument<? super ProbeMutatorParams>> getArguments() {
		Collection<Argument<? super ProbeMutatorParams>> args = new ArrayList<Argument<? super ProbeMutatorParams>>();
		args.add(new ProbesArgument(false));
		args.add(new KmerArgument(false));
		args.add(new EscoreArgument(true, 0.3));
		args.add(new BindingBarrierArgument(true));
		return args;
	}

	@Override
	public Data execute(ProgressListener l, ProbeMutatorParams params) throws Exception {
		List<Probe> mutated = new ArrayList<Probe>();
		util.genome.kmer.Kmer kmer = params.getKmers().getKmer();
		int bindingBarrier = params.BINDING_SITE_BARRIER;
		double cutoff = params.getEscore();
		for(Probe p : params.getProbes().getProbeGroup()){
			Probe mut = ProbeUtils.mutate(l, p, kmer, DNA_ALPHABET, bindingBarrier, cutoff);
			mutated.add(mut);
		}
		return new Probes(new ProbeGroup(mutated));
	}

}
