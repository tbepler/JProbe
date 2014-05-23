package chiptools.jprobe.function.args;

import chiptools.Constants;
import chiptools.jprobe.function.params.EscoreParam;
import jprobe.services.function.DoubleArgument;

public class EscoreArgument extends DoubleArgument<EscoreParam>{

	public EscoreArgument(boolean optional) {
		super(
				Constants.getName(EscoreArgument.class),
				Constants.getDescription(EscoreArgument.class),
				Constants.getCategory(EscoreArgument.class),
				Constants.getFlag(EscoreArgument.class),
				Constants.getPrototypeValue(EscoreArgument.class),
				optional,
				0.4,
				-0.5,
				0.5,
				0.1
				);
	}

	@Override
	protected void process(EscoreParam params, Double value) {
		params.setEscore(value);
	}

}
