package chiptools.jprobe.function;

import chiptools.Resources;
import jprobe.services.function.FlagArgument;
import jprobe.services.function.Function;

public abstract class ChiptoolsFlagArg<P> extends FlagArgument<P> {

	@SuppressWarnings("rawtypes")
	protected ChiptoolsFlagArg(Class<? extends Function> funcClass, Class<? extends ChiptoolsFlagArg> clazz) {
		super(
				Resources.getArgumentName(funcClass, clazz),
				Resources.getArgumentDescription(funcClass, clazz),
				Resources.getArgumentCategory(funcClass, clazz),
				Resources.getArgumentFlag(funcClass, clazz)
				);
	}

}
