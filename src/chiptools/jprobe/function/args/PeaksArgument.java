package chiptools.jprobe.function.args;

import java.util.List;

import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.ChiptoolsDataArg;
import chiptools.jprobe.function.params.PeaksParam;
import jprobe.services.function.Function;

public class PeaksArgument extends ChiptoolsDataArg<PeaksParam, Peaks>{

	public PeaksArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				PeaksArgument.class,
				Peaks.class,
				optional,
				1,
				1,
				false
				);
	}

	@Override
	protected void process(PeaksParam params, List<Peaks> data) {
		params.setPeaks(data.get(0));
	}

}
