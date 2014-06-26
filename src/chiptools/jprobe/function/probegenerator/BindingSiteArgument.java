package chiptools.jprobe.function.probegenerator;

import chiptools.jprobe.function.ChiptoolsIntArg;
import jprobe.services.function.Function;

public class BindingSiteArgument extends ChiptoolsIntArg<ProbeGeneratorParams>{

	public BindingSiteArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				BindingSiteArgument.class,
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
