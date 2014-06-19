package chiptools.jprobe.function.negativecontrolgen;

import chiptools.jprobe.function.ChiptoolsIntArg;

public class NumberArg extends ChiptoolsIntArg<NegControlParams>{

	public NumberArg(boolean optional) {
		super(
				NumberArg.class,
				"unlimited",
				optional,
				500,
				0,
				Integer.MAX_VALUE,
				10
				);
	}

	@Override
	protected void process(NegControlParams params, Integer value) {
		params.setNumPeaks(value);
	}

}
