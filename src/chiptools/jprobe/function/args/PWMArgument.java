package chiptools.jprobe.function.args;

import java.util.List;

import chiptools.jprobe.data.PWM;
import chiptools.jprobe.function.ChiptoolsDataArg;
import chiptools.jprobe.function.params.PWMParam;
import jprobe.services.function.Function;

public class PWMArgument extends ChiptoolsDataArg<PWMParam, PWM>{

	public PWMArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				PWMArgument.class,
				PWM.class,
				optional,
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
