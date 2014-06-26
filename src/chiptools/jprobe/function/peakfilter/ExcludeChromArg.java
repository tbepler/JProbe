package chiptools.jprobe.function.peakfilter;

import java.util.Collection;

import jprobe.services.function.Function;
import util.genome.Chromosome;
import util.genome.peak.Peak;
import util.genome.peak.PeakUtils.Filter;
import chiptools.jprobe.function.ChromsArg;

public class ExcludeChromArg extends ChromsArg<PeakFilterParams>{

	public ExcludeChromArg(Function<?> parent, boolean optional) {
		super(parent.getClass(), ExcludeChromArg.class, "off", optional, "");
	}

	@Override
	protected void process(PeakFilterParams params, final Collection<Chromosome> chroms) {
		params.addFilter(new Filter(){

			@Override
			public boolean keep(Peak p) {
				for(Chromosome chrom : chroms){
					if(p.getChrom().equals(chrom)){
						return false;
					}
				}
				return true;
			}
			
		});
		
	}

}
