package chiptools.jprobe.function.probefilter;

import java.util.Collection;

import jprobe.services.function.Function;
import util.genome.Chromosome;
import util.genome.probe.Probe;
import util.genome.probe.ProbeUtils.Filter;
import chiptools.jprobe.function.ChromsArg;

public class IncludeChromsArg extends ChromsArg<ProbeFilterParam>{

	protected IncludeChromsArg(Function<?> parent, boolean optional) {
		super(parent.getClass(), IncludeChromsArg.class, "off", optional, "");
	}

	@Override
	protected void process(ProbeFilterParam params, final Collection<Chromosome> chroms) {
		params.addFilter(new Filter(){

			@Override
			public boolean keep(Probe p) {
				for(Chromosome chrom : chroms){
					if(p.getRegion().getChromosome().equals(chrom)){
						return true;
					}
				}
				return false;
			}
			
		});
	}

	

}
