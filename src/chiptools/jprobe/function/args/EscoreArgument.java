package chiptools.jprobe.function.args;

import chiptools.Constants;
import chiptools.jprobe.function.params.EscoreParam;
import jprobe.services.function.DoubleArgument;

public class EscoreArgument extends DoubleArgument<EscoreParam>{

	public EscoreArgument(boolean optional, Double defaultVal) {
		super(
				Constants.getName(EscoreArgument.class),
				Constants.getDescription(EscoreArgument.class) + (defaultVal == null ? "" : ", default="+defaultVal),
				Constants.getCategory(EscoreArgument.class),
				Constants.getFlag(EscoreArgument.class),
				Constants.getPrototypeValue(EscoreArgument.class) + (defaultVal == null ? "" : "{default="+defaultVal+"}"),
				optional,
				defaultVal,
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
