package chiptools.jprobe.function.args;

import java.util.List;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.ChiptoolsDataArg;
import chiptools.jprobe.function.params.ProbesParam;
import jprobe.services.function.Function;

public class ProbesArgument extends ChiptoolsDataArg<ProbesParam, Probes> {

	public ProbesArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				ProbesArgument.class,
				Probes.class,
				optional,
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
