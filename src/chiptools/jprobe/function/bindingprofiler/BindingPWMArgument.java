package chiptools.jprobe.function.bindingprofiler;

import java.util.List;

import util.progress.ProgressListener;
import chiptools.jprobe.data.PWM;
import chiptools.jprobe.function.ChiptoolsDataArg;
import jprobe.services.function.Function;

public class BindingPWMArgument extends ChiptoolsDataArg<BindingProfileParams, PWM>{

	public BindingPWMArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				BindingPWMArgument.class,
				PWM.class,
				optional,
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
