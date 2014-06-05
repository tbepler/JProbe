package chiptools.jprobe.function;

import chiptools.Constants;
import jprobe.services.function.StringArgument;

public abstract class ChiptoolsTextArg<P> extends StringArgument<P>{

	@SuppressWarnings("rawtypes")
	protected ChiptoolsTextArg(Class<? extends ChiptoolsTextArg> clazz, String defaultVal, boolean optional, String startValue) {
		super(
				Constants.getName(clazz),
				Constants.getDescription(clazz) + (optional && defaultVal != null ? ", default="+defaultVal : ""),
				Constants.getCategory(clazz),
				Constants.getFlag(clazz),
				Constants.getPrototypeValue(clazz) + (optional && defaultVal != null ? "{default="+defaultVal+"}" : ""),
				optional,
				startValue
				);
	}

}
