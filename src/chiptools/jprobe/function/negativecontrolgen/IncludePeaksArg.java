package chiptools.jprobe.function.negativecontrolgen;

import java.util.List;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.Peaks;
import jprobe.services.function.DataArgument;

public class IncludePeaksArg extends DataArgument<NegControlParams, Peaks>{

	public IncludePeaksArg(boolean optional) {
		super(
				ChiptoolsActivator.getCore(),
				Constants.getName(IncludePeaksArg.class),
				Constants.getDescription(IncludePeaksArg.class),
				Constants.getCategory(IncludePeaksArg.class),
				Constants.getFlag(IncludePeaksArg.class),
				optional,
				Peaks.class,
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
