package chiptools.jprobe.function;

import jprobe.services.data.Data;
import jprobe.services.function.DataArgument;
import jprobe.services.function.Function;
import chiptools.Resources;
import chiptools.jprobe.ChiptoolsActivator;

public abstract class ChiptoolsDataArg<P,D extends Data> extends DataArgument<P,D> {

	@SuppressWarnings("rawtypes")
	protected ChiptoolsDataArg(
			Class<? extends Function> funcClass,
			Class<? extends ChiptoolsDataArg> clazz,
			Class<D> dataClass,
			boolean optional,
			int minArgs,
			int maxArgs,
			boolean allowDuplicates
			) {
		super(
				ChiptoolsActivator.getCore(),
				Resources.getArgumentName(funcClass, clazz),
				Resources.getArgumentDescription(funcClass, clazz),
				Resources.getArgumentCategory(funcClass, clazz),
				Resources.getArgumentFlag(funcClass, clazz),
				optional,
				dataClass,
				minArgs,
				maxArgs,
				allowDuplicates
				);
	}

}
