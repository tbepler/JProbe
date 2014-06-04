package chiptools.jprobe.function.probefilter;

import java.util.Random;

import chiptools.jprobe.function.ChiptoolsIntArg;

public class RandomSeedArgument extends ChiptoolsIntArg<ProbeFilterParam>{

	protected RandomSeedArgument(boolean optional) {
		super(RandomSeedArgument.class, "off", optional, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
	}

	@Override
	protected void process(ProbeFilterParam params, Integer value) {
		params.setRandom(new Random(value));
	}

}
