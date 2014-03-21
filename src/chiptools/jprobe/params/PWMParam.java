package chiptools.jprobe.params;

import chiptools.jprobe.data.PWM;
import jprobe.services.data.Data;
import jprobe.services.function.DataParameter;

public class PWMParam implements DataParameter{

	@Override
	public String getName() {
		return "PWM";
	}

	@Override
	public String getDescription() {
		return "Position weight matrix data";
	}

	@Override
	public boolean isOptional() {
		return false;
	}

	@Override
	public Class<? extends Data> getType() {
		return PWM.class;
	}

	@Override
	public boolean isValid(Data data) {
		return data instanceof PWM;
	}

}
