package chiptools.jprobe.function.negativecontrolgen;

import java.util.List;

import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.ChiptoolsDataArg;
import jprobe.services.function.Function;

public class IncludePeaksArg extends ChiptoolsDataArg<NegControlParams, Peaks>{

	public IncludePeaksArg(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				IncludePeaksArg.class,
				Peaks.class,
				optional,
				1,
				Integer.MAX_VALUE,
				false
				);
	}

	@Override
	protected void process(NegControlParams params, List<Peaks> data) {
		params.setIncludePeaks(data);
	}

}
