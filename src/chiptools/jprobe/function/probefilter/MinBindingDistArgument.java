package chiptools.jprobe.function.probefilter;

import jprobe.services.function.Function;
import util.genome.GenomicRegion;
import util.genome.probe.Probe;
import util.genome.probe.ProbeUtils.Filter;
import chiptools.jprobe.function.ChiptoolsIntArg;

public class MinBindingDistArgument extends ChiptoolsIntArg<ProbeFilterParam>{

	protected MinBindingDistArgument(Function<?> parent, boolean optional) {
		super(parent.getClass(), MinBindingDistArgument.class, "off", optional, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
	}

	@Override
	protected void process(ProbeFilterParam params, Integer value) {
		final int min = value;
		params.addFilter(new Filter(){

			@Override
			public boolean keep(Probe p) {
				GenomicRegion[] bindingSites = p.getBindingSites();
				for(int i=0; i<bindingSites.length - 1; i++){
					GenomicRegion cur = bindingSites[i];
					GenomicRegion next = bindingSites[i+1];
					if(cur.distance(next) < min){
						return false;
					}
				}
				return true;
			}
			
		});
	}

}
