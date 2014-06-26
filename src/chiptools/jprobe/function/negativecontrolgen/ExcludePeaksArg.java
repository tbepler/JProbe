package chiptools.jprobe.function.negativecontrolgen;

import java.util.List;

import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.ChiptoolsDataArg;
import jprobe.services.function.Function;

public class ExcludePeaksArg extends ChiptoolsDataArg<NegControlParams, Peaks>{
	
	public ExcludePeaksArg(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				ExcludePeaksArg.class,
				Peaks.class,
				optional,
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
