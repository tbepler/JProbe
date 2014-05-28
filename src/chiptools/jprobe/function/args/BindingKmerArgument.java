package chiptools.jprobe.function.args;

import java.util.List;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.function.params.BindingProfileParams;
import jprobe.services.function.DataArgument;

public class BindingKmerArgument extends DataArgument<BindingProfileParams, Kmer>{

	public BindingKmerArgument(boolean optional) {
		super(
				ChiptoolsActivator.getCore(),
				Constants.getName(BindingKmerArgument.class),
				Constants.getDescription(BindingKmerArgument.class),
				Constants.getCategory(BindingKmerArgument.class),
				Constants.getFlag(BindingKmerArgument.class),
				optional,
				Kmer.class,
				0,
				Integer.MAX_VALUE,
				false
				);
	}

	@Override
	protected void process(BindingProfileParams params, List<Kmer> data) {
		params.setKmers(data);
		params.KMER_NAMES.clear();
		for(Kmer k : data){
			params.KMER_NAMES.add(ChiptoolsActivator.getCore().getDataManager().getDataName(k));
		}
	}


}
