package chiptools.jprobe.function.args;

import java.util.List;

import chiptools.jprobe.data.PeakSequences;
import chiptools.jprobe.function.ChiptoolsDataArg;
import chiptools.jprobe.function.params.PeakSeqsParam;
import jprobe.services.function.Function;

public class PeakSeqsArgument extends ChiptoolsDataArg<PeakSeqsParam, PeakSequences>{

	public PeakSeqsArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				PeakSeqsArgument.class,
				PeakSequences.class,
				optional,
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
