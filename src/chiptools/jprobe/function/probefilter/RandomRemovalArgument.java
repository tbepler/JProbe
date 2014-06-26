package chiptools.jprobe.function.probefilter;

import jprobe.services.function.Function;
import chiptools.jprobe.function.ChiptoolsIntArg;

public class RandomRemovalArgument extends ChiptoolsIntArg<ProbeFilterParam>{
	
	public RandomRemovalArgument(Function<?> parent, boolean optional) {
		super(parent.getClass(), RandomRemovalArgument.class, "off", optional, 0, 0, Integer.MAX_VALUE, 1);
	}
	
	@Override
	protected void process(ProbeFilterParam params, Integer value) {
		params.setRemove(value);
	}

}
