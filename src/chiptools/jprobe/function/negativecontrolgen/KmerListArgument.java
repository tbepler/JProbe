package chiptools.jprobe.function.negativecontrolgen;

import java.util.List;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.function.params.KmerListParam;
import jprobe.services.function.DataArgument;

public class KmerListArgument extends DataArgument<KmerListParam, Kmer>{

	public KmerListArgument(boolean optional) {
		super(
				ChiptoolsActivator.getCore(),
				Constants.getName(KmerListArgument.class),
				Constants.getDescription(KmerListArgument.class),
				Constants.getCategory(KmerListArgument.class),
				Constants.getFlag(KmerListArgument.class),
				optional,
				Kmer.class,
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
