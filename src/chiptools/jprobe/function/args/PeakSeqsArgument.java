package chiptools.jprobe.function.args;

import java.util.List;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.PeakSequences;
import chiptools.jprobe.function.params.PeakSeqsParam;
import jprobe.services.function.DataArgument;

public class PeakSeqsArgument extends DataArgument<PeakSeqsParam, PeakSequences>{

	public PeakSeqsArgument(boolean optional) {
		super(
				ChiptoolsActivator.getCore(),
				Constants.getName(PeakSeqsArgument.class),
				Constants.getDescription(PeakSeqsArgument.class),
				Constants.getCategory(PeakSeqsArgument.class),
				Constants.getFlag(PeakSeqsArgument.class),
				optional,
				PeakSequences.class,
				1,
				1,
				false
				);
	}

	@Override
	protected void process(PeakSeqsParam params, List<PeakSequences> data) {
		params.setPeakSeqs(data.get(0));
	}

}
