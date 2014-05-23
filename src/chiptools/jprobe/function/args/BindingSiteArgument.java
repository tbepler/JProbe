package chiptools.jprobe.function.args;

import chiptools.Constants;
import chiptools.jprobe.function.params.ProbeGeneratorParams;
import jprobe.services.function.IntArgument;

public class BindingSiteArgument extends IntArgument<ProbeGeneratorParams>{

	public BindingSiteArgument(boolean optional) {
		super(
				Constants.getName(BindingSiteArgument.class),
				Constants.getDescription(BindingSiteArgument.class),
				Constants.getCategory(BindingSiteArgument.class), 
				Constants.getFlag(BindingSiteArgument.class),
				Constants.getPrototypeValue(BindingSiteArgument.class),
				optional,
				9,
				1,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(ProbeGeneratorParams params, Integer value) {
		params.BINDINGSITE = value;
	}

}
