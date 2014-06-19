package chiptools.jprobe.function.negativecontrolgen;

import java.util.List;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.Peaks;
import jprobe.services.function.DataArgument;

public class ExcludePeaksArg extends DataArgument<NegControlParams, Peaks>{
	
	public ExcludePeaksArg(boolean optional) {
		super(
				ChiptoolsActivator.getCore(),
				Constants.getName(ExcludePeaksArg.class),
				Constants.getDescription(ExcludePeaksArg.class),
				Constants.getCategory(ExcludePeaksArg.class),
				Constants.getFlag(ExcludePeaksArg.class),
				optional,
				Peaks.class,
				0,
				Integer.MAX_VALUE,
				false
				);
	}

	@Override
	protected void process(NegControlParams params, List<Peaks> data) {
		params.setExcludePeaks(data);
	}

}
