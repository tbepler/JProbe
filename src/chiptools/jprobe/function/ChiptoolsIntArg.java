package chiptools.jprobe.function;

import chiptools.Constants;
import jprobe.services.function.IntArgument;

public abstract class ChiptoolsIntArg<P> extends IntArgument<P> {

	@SuppressWarnings("rawtypes")
	protected ChiptoolsIntArg(Class<? extends ChiptoolsIntArg> clazz, String defaultVal, boolean optional, int startValue, int min, int max, int increment) {
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
	protected ChiptoolsIntArg(Class<? extends ChiptoolsIntArg> clazz, boolean optional, int startValue, int min, int max, int increment){
		this(clazz, String.valueOf(startValue), optional, startValue, min, max, increment);
	}

}
