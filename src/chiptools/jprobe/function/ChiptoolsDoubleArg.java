package chiptools.jprobe.function;

import chiptools.Constants;
import jprobe.services.function.DoubleArgument;

public abstract class ChiptoolsDoubleArg<P> extends DoubleArgument<P> {

	@SuppressWarnings("rawtypes")
	protected ChiptoolsDoubleArg(Class<? extends ChiptoolsDoubleArg> clazz, String defaultVal, boolean optional, double startValue, double min, double max, double increment) {
		super(
				Constants.getName(clazz),
				Constants.getDescription(clazz) + (optional ? ", default="+defaultVal : ""),
				Constants.getCategory(clazz),
				Constants.getFlag(clazz),
				Constants.getPrototypeValue(clazz) + (optional ? "{default="+defaultVal+"}" : ""),
				optional,
				startValue,
				min,
				max,
				increment
				);
	}
	
	@SuppressWarnings("rawtypes")
	protected ChiptoolsDoubleArg(Class<? extends ChiptoolsDoubleArg> clazz, boolean optional, double startValue, double min, double max, double increment){
		this(clazz, String.valueOf(startValue), optional, startValue, min, max, increment);
	}

}
