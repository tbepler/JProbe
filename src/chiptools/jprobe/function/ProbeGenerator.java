package chiptools.jprobe.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.genome.GenomicSequence;
import util.genome.peak.PeakSequence;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.genome.probe.ProbeUtils;
import util.progress.ProgressListener;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.ProbeGeneratorParams;
import chiptools.jprobe.function.args.*;

public class ProbeGenerator extends AbstractChiptoolsFunction<ProbeGeneratorParams>{

	public ProbeGenerator() {
		super(ProbeGeneratorParams.class);
	}

	@Override
	public Collection<Argument<? super ProbeGeneratorParams>> getArguments() {
		Collection<Argument<? super ProbeGeneratorParams>> args = new ArrayList<Argument<? super ProbeGeneratorParams>>();
		
		args.add(new PeakSeqsArgument(false));
		args.add(new KmerArgument(false));
		args.add(new PWMArgument(false));
		
		args.add(new ProbeLengthArgument(true));
		args.add(new BindingSiteArgument(true));
		args.add(new WindowSizeArgument(true));
		args.add(new EscoreArgument(true));
		
		return args;
	}

	@Override
	public Data execute(ProgressListener l, ProbeGeneratorParams params) throws Exception {
		
		List<GenomicSequence> seqs = new ArrayList<GenomicSequence>();
		List<String> names = new ArrayList<String>();
		for(PeakSequence p : params.getPeakSeqs().getPeakSeqs()){
			seqs.add(p.getGenomicSequence());
			names.add(p.getName());
		}
		
		Queue<Probe> probes = new PriorityQueue<Probe>();
		for(int i=0; i<seqs.size(); i++){
			try{
				GenomicSequence seq = seqs.get(i);
				String name = names.get(i);
				probes.addAll(ProbeUtils.extractFrom(
						seq,
						name,
						params.getKmers().getKmer(),
						params.getPWM().getPWM(),
						params.getProbeLength(),
						params.BINDINGSITE,
						params.WINDOWSIZE,
						params.getEscore()
						));
			} catch (Exception e){
				throw e;
			}
		}
		ProbeGroup group = new ProbeGroup(probes);
		return new Probes(group);
	}

}
