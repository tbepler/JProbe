package chiptools.jprobe.function.negativecontrolgen;

import java.util.List;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.function.ChiptoolsDataArg;
import chiptools.jprobe.function.params.KmerListParam;
import jprobe.services.function.Function;

public class KmerListArgument extends ChiptoolsDataArg<KmerListParam, Kmer>{

	public KmerListArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				KmerListArgument.class,
				Kmer.class,
				optional,
				0,
				Integer.MAX_VALUE,
				false
				);
	}

	@Override
	protected void process(KmerListParam params, List<Kmer> data) {
		params.setKmers(data);
	}

}
