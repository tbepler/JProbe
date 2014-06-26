package chiptools.jprobe.function.args;

import java.util.List;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.function.ChiptoolsDataArg;
import chiptools.jprobe.function.params.KmerParam;
import jprobe.services.function.Function;

public class KmerArgument extends ChiptoolsDataArg<KmerParam, Kmer>{

	public KmerArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				KmerArgument.class,
				Kmer.class,
				optional,
				1,
				1,
				false
				);
	}

	@Override
	protected void process(KmerParam params, List<Kmer> data) {
		params.setKmers(data.get(0));
	}

}
