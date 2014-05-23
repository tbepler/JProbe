package chiptools.jprobe.function.args;

import chiptools.Constants;
import chiptools.jprobe.function.params.SummitParam;
import jprobe.services.function.IntArgument;

public class SummitArgument extends IntArgument<SummitParam>{

	public SummitArgument(boolean optional) {
		super(
				Constants.getName(SummitArgument.class),
				Constants.getDescription(SummitArgument.class),
				Constants.getCategory(SummitArgument.class),
				Constants.getFlag(SummitArgument.class),
				Constants.getPrototypeValue(SummitArgument.class),
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
