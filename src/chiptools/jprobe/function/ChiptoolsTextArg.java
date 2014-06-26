package chiptools.jprobe.function;

import chiptools.Resources;
import jprobe.services.function.Function;
import jprobe.services.function.StringArgument;

public abstract class ChiptoolsTextArg<P> extends StringArgument<P>{

	@SuppressWarnings("rawtypes")
	protected ChiptoolsTextArg(Class<? extends Function> funcClass, Class<? extends ChiptoolsTextArg> clazz, String defaultVal, boolean optional, String startValue) {
		super(
				Resources.getArgumentName(funcClass, clazz),
				Resources.getArgumentDescription(funcClass, clazz) + (optional ? " Default="+defaultVal : ""),
				Resources.getArgumentCategory(funcClass, clazz),
				Resources.getArgumentFlag(funcClass, clazz),
				Resources.getArgumentPrototype(funcClass, clazz) + (optional ? "{default="+defaultVal+"}" : ""),
				optional,
				startValue
				);
	}

}
