package chiptools.jprobe.function.args;

import java.util.List;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.function.params.KmerParam;
import jprobe.services.function.DataArgument;

public class KmerArgument extends DataArgument<KmerParam, Kmer>{

	public KmerArgument(boolean optional) {
		super(
				ChiptoolsActivator.getCore(),
				Constants.getName(KmerArgument.class),
				Constants.getDescription(KmerArgument.class),
				Constants.getCategory(KmerArgument.class),
				Constants.getFlag(KmerArgument.class),
				optional,
				Kmer.class,
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
