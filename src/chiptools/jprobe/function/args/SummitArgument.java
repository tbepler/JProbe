package chiptools.jprobe.function.args;

import chiptools.jprobe.function.ChiptoolsIntArg;
import chiptools.jprobe.function.params.SummitParam;
import jprobe.services.function.Function;

public class SummitArgument extends ChiptoolsIntArg<SummitParam>{

	public SummitArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				SummitArgument.class,
				"off",
				optional,
				0,
				0,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(SummitParam params, Integer value) {
		params.setSummit(value);
	}

}
