package chiptools.jprobe.function.args;

import java.util.List;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.ProbesParam;
import jprobe.services.function.DataArgument;

public class ProbesArgument extends DataArgument<ProbesParam, Probes> {

	public ProbesArgument(boolean optional) {
		super(
				ChiptoolsActivator.getCore(),
				Constants.getName(ProbesArgument.class),
				Constants.getDescription(ProbesArgument.class),
				Constants.getCategory(ProbesArgument.class),
				Constants.getFlag(ProbesArgument.class),
				optional,
				Probes.class,
				1,
				1,
				false
				);
	}

	@Override
	protected void process(ProbesParam params, List<Probes> data) {
		params.setProbes(data.get(0));
	}

}
