package chiptools.jprobe.function.bindingprofiler;

import java.util.List;

import util.progress.ProgressListener;
import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.PWM;
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
	}
	
	@Override
	public void parse(ProgressListener l, BindingProfileParams params, String[] args){
		params.PWM_NAMES.clear();
		for(String s : args){
			params.PWM_NAMES.add(s);
		}
		super.parse(l, params, args);
	}

}
