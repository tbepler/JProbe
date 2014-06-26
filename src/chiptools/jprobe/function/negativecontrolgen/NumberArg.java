package chiptools.jprobe.function.negativecontrolgen;

import jprobe.services.function.Function;
import chiptools.jprobe.function.ChiptoolsIntArg;

public class NumberArg extends ChiptoolsIntArg<NegControlParams>{

	public NumberArg(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
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
