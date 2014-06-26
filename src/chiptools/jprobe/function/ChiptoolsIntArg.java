package chiptools.jprobe.function;

import chiptools.Resources;
import jprobe.services.function.Function;
import jprobe.services.function.IntArgument;

public abstract class ChiptoolsIntArg<P> extends IntArgument<P> {

	@SuppressWarnings("rawtypes")
	protected ChiptoolsIntArg(
			Class<? extends Function> funcClass,
			Class<? extends ChiptoolsIntArg> clazz,
			String defaultVal,
			boolean optional,
			int startValue,
			int min,
			int max,
			int increment
			) {
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
	protected ChiptoolsIntArg(Class<? extends Function> funcClass, Class<? extends ChiptoolsIntArg> clazz, boolean optional, int startValue, int min, int max, int increment){
		this(funcClass, clazz, String.valueOf(startValue), optional, startValue, min, max, increment);
	}

}
