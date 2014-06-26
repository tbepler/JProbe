package chiptools.jprobe.function.args;

import chiptools.jprobe.function.ChiptoolsDoubleArg;
import chiptools.jprobe.function.params.EscoreParam;
import jprobe.services.function.Function;

public class EscoreArgument extends ChiptoolsDoubleArg<EscoreParam>{

	public EscoreArgument(Function<?> parent, boolean optional, Double defaultVal) {
		super(
				parent.getClass(),
				EscoreArgument.class,
				String.valueOf(defaultVal),
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
