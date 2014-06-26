package chiptools.jprobe.function.probefilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.genome.probe.ProbeUtils;
import util.progress.ProgressListener;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.ProbesArgument;

public class ProbeFilter extends AbstractChiptoolsFunction<ProbeFilterParam>{

	public ProbeFilter() {
		super(ProbeFilterParam.class);
	}

	@Override
	public Collection<Argument<? super ProbeFilterParam>> getArguments() {
		Collection<Argument<? super ProbeFilterParam>> args = new ArrayList<Argument<? super ProbeFilterParam>>();
		args.add(new ProbesArgument(this, false));
		args.add(new MinMutationsArgument(this, true));
		args.add(new MaxMutationsArgument(this, true));
		args.add(new MinBindingDistArgument(this, true));
		args.add(new MaxBindingDistArgument(this, true));
		args.add(new MinBindingSitesArg(this, true));
		args.add(new MaxBindingSitesArg(this, true));
		args.add(new IncludeSubseqArgument(this, true));
		args.add(new ExcludeSubseqArgument(this, true));
		args.add(new IncludeChromsArg(this, true));
		args.add(new ExcludeChromsArg(this, true));
		args.add(new RandomRemovalArgument(this, true));
		args.add(new RandomSeedArgument(this, true));
		
		return args;
	}

	@Override
	public Data execute(ProgressListener l, ProbeFilterParam params) throws Exception {
		ProbeGroup probes = params.getProbes().getProbeGroup();
		ProbeGroup filtered = ProbeUtils.filter(probes, params);
		if(params.getRemove() > 0){
			List<Probe> list = filtered.toList();
			Random r = params.getRandom();
			for(int i=0; i<params.getRemove(); i++){
				int remove = r.nextInt(list.size());
				list.remove(remove);
			}
			filtered = new ProbeGroup(list);
		}
		return new Probes(filtered);
	}

}
