package chiptools.jprobe.function;

import chiptools.Resources;
import jprobe.services.function.DoubleArgument;
import jprobe.services.function.Function;

public abstract class ChiptoolsDoubleArg<P> extends DoubleArgument<P> {

	@SuppressWarnings("rawtypes")
	protected ChiptoolsDoubleArg(Class<? extends Function> funcClass, Class<? extends ChiptoolsDoubleArg> clazz, String defaultVal, boolean optional, double startValue, double min, double max, double increment) {
		super(
				Resources.getArgumentName(funcClass, clazz),
				Resources.getArgumentDescription(funcClass, clazz) + (optional ? " Default="+defaultVal : ""),
				Resources.getArgumentCategory(funcClass, clazz),
				Resources.getArgumentFlag(funcClass, clazz),
				Resources.getArgumentPrototype(funcClass, clazz) + (optional ? "{default="+defaultVal+"}" : ""),
				optional,
				startValue,
				min,
				max,
				increment
				);
	}
	
	@SuppressWarnings("rawtypes")
	protected ChiptoolsDoubleArg(Class<? extends Function> funcClass, Class<? extends ChiptoolsDoubleArg> clazz, boolean optional, double startValue, double min, double max, double increment){
		this(funcClass, clazz, String.valueOf(startValue), optional, startValue, min, max, increment);
	}

}
