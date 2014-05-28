package chiptools.jprobe.function.args;

import java.util.List;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.PWM;
import chiptools.jprobe.function.params.BindingProfileParams;
import jprobe.services.function.DataArgument;

public class BindingPWMArgument extends DataArgument<BindingProfileParams, PWM>{

	public BindingPWMArgument(boolean optional) {
		super(
				ChiptoolsActivator.getCore(),
				Constants.getName(BindingPWMArgument.class),
				Constants.getDescription(BindingPWMArgument.class),
				Constants.getCategory(BindingPWMArgument.class),
				Constants.getFlag(BindingPWMArgument.class),
				optional,
				PWM.class,
				0,
				Integer.MAX_VALUE,
				false
				);
	}

	@Override
	protected void process(BindingProfileParams params, List<PWM> data) {
		params.setPWMs(data);
		params.PWM_NAMES.clear();
		for(PWM p : data){
			params.PWM_NAMES.add(ChiptoolsActivator.getCore().getDataManager().getDataName(p));
		}
	}

}
