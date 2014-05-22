package chiptools.jprobe.function.args;

import java.util.List;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.PWM;
import chiptools.jprobe.function.params.PWMParam;
import jprobe.services.function.DataArgument;

public class PWMArgument extends DataArgument<PWMParam, PWM>{

	public PWMArgument(boolean optional) {
		super(
				ChiptoolsActivator.getCore(),
				Constants.getName(PWMArgument.class),
				Constants.getDescription(PWMArgument.class),
				Constants.getCategory(PWMArgument.class),
				optional,
				PWM.class,
				1,
				1,
				false
				);
	}

	@Override
	protected void process(PWMParam params, List<PWM> data) {
		params.setPWM(data.get(0));
	}

}
