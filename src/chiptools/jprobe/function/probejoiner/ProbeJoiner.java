package chiptools.jprobe.function.probejoiner;

import java.util.ArrayList;
import java.util.Collection;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.genome.probe.ProbeUtils;
import util.progress.ProgressListener;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.ProbeLengthArgument;
import chiptools.jprobe.function.args.ProbesArgument;

public class ProbeJoiner extends AbstractChiptoolsFunction<ProbeJoinerParams>{

	public ProbeJoiner() {
		super(ProbeJoinerParams.class);
	}

	@Override
	public Collection<Argument<? super ProbeJoinerParams>> getArguments() {
		Collection<Argument<? super ProbeJoinerParams>> args = new ArrayList<Argument<? super ProbeJoinerParams>>();
		args.add(new ProbesArgument(this, false));
		args.add(new NumBindingSitesArgument(this, false));
		args.add(new MinSiteDistArgument(this, true));
		args.add(new MaxSiteDistArgument(this, true));
		args.add(new ProbeLengthArgument(this, true, "unbounded"));
		return args;
	}

	@Override
	public Data execute(ProgressListener l, ProbeJoinerParams params) throws Exception {
		Probes p = params.getProbes();
		Probes combined;
		if(params.getProbeLength() > 0){
			combined = new Probes(ProbeUtils.joinProbes(
					l,
					p.getProbeGroup(),
					params.NUMBINDINGSITES,
					params.MINSITEDIST,
					params.MAXSITEDIST,
					params.getProbeLength()
					));
		}else{
			combined = new Probes(ProbeUtils.joinProbes(
					l,
					p.getProbeGroup(),
					params.NUMBINDINGSITES,
					params.MINSITEDIST,
					params.MAXSITEDIST
					));
		}
		return combined;
	}

}
