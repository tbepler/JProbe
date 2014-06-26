package chiptools.jprobe.function.probefilter;

import jprobe.services.function.Function;
import util.genome.GenomicRegion;
import util.genome.probe.Probe;
import util.genome.probe.ProbeUtils.Filter;
import chiptools.jprobe.function.ChiptoolsIntArg;

public class MaxBindingDistArgument extends ChiptoolsIntArg<ProbeFilterParam>{

	protected MaxBindingDistArgument(Function<?> parent, boolean optional) {
		super(parent.getClass(), MaxBindingDistArgument.class, "off", optional, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
	}

	@Override
	protected void process(ProbeFilterParam params, Integer value) {
		final int max = value;
		params.addFilter(new Filter(){

			@Override
			public boolean keep(Probe p) {
				GenomicRegion[] bindingSites = p.getBindingSites();
				for(int i=0; i<bindingSites.length - 1; i++){
					GenomicRegion cur = bindingSites[i];
					GenomicRegion next = bindingSites[i+1];
					if(cur.distance(next) > max){
						return false;
					}
				}
				return true;
			}
			
		});
	}

}
