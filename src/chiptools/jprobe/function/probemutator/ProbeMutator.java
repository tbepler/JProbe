package chiptools.jprobe.function.probemutator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	private static final Set<Character> DNA_ALPHABET = generateAlphabet();
			
	private static Set<Character> generateAlphabet(){
		Set<Character> alph = new HashSet<Character>();
		alph.add('A');
		alph.add('C');
		alph.add('G');
		alph.add('T');
		return Collections.unmodifiableSet(alph);
	}

	public ProbeMutator() {
		super(ProbeMutatorParams.class);
	}

	@Override
	public Collection<Argument<? super ProbeMutatorParams>> getArguments() {
		Collection<Argument<? super ProbeMutatorParams>> args = new ArrayList<Argument<? super ProbeMutatorParams>>();
		args.add(new ProbesArgument(false));
		args.add(new KmerArgument(false));
		args.add(new PrimerArgument(true));
		args.add(new EscoreArgument(true, 0.3));
		args.add(new BindingBarrierArgument(true));
		args.add(new MutateBindingSitesArgument());
		return args;
	}
	
	protected Probe mutate(ProgressListener l, Probe p, util.genome.kmer.Kmer kmer, int bindingBarrier, double cutoff, String primer){
		Probe mut;
		if(primer != null){
			mut = ProbeUtils.mutate(l, p, kmer, DNA_ALPHABET, bindingBarrier, cutoff, primer);
		}else{
			mut = ProbeUtils.mutate(l, p, kmer, DNA_ALPHABET, bindingBarrier, cutoff);
		}
		return mut;
	}
	
	protected List<Probe> generateBindingSitePermutations(Probe p){
		return ProbeUtils.generateBindingSitePermuations(p);
	}

	@Override
	public Data execute(ProgressListener l, ProbeMutatorParams params) throws Exception {
		List<Probe> mutated = new ArrayList<Probe>();
		util.genome.kmer.Kmer kmer = params.getKmers().getKmer();
		int bindingBarrier = params.BINDING_SITE_BARRIER;
		double cutoff = params.getEscore();
		String primer = params.getPrimer();
		for(Probe p : params.getProbes().getProbeGroup()){
			if(params.MUTATE_BINDING_SITES){
				for(Probe permutation : this.generateBindingSitePermutations(p)){
					mutated.add(this.mutate(l, permutation, kmer, bindingBarrier, cutoff, primer));
				}
			}else{
				mutated.add(this.mutate(l, p, kmer, bindingBarrier, cutoff, primer));
			}
		}
		return new Probes(new ProbeGroup(mutated));
	}

}
