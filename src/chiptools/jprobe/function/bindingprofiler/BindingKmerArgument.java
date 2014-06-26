package chiptools.jprobe.function.bindingprofiler;

import java.util.List;

import util.progress.ProgressListener;
import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.function.ChiptoolsDataArg;
import jprobe.services.function.Function;

public class BindingKmerArgument extends ChiptoolsDataArg<BindingProfileParams, Kmer>{

	public BindingKmerArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				BindingKmerArgument.class,
				Kmer.class,
				optional,
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
