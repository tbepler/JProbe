package chiptools.jprobe.function.bindingprofiler;

import java.util.List;

import util.progress.ProgressListener;
import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.Kmer;
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
	}
	
	@Override
	public void parse(ProgressListener l, BindingProfileParams params, String[] args){
		params.KMER_NAMES.clear();
		for(String s : args){
			params.KMER_NAMES.add(s);
		}
		super.parse(l, params, args);
	}


}
