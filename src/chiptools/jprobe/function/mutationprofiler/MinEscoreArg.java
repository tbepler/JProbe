package chiptools.jprobe.function.mutationprofiler;

import chiptools.jprobe.function.ChiptoolsDoubleArg;

public class MinEscoreArg extends ChiptoolsDoubleArg<MutationProfilerParams>{

	public MinEscoreArg(boolean optional) {
		super(MinEscoreArg.class, "off", optional, 0.35, -0.5, 0.5, 0.05);
	}

	@Override
	protected void process(MutationProfilerParams params, Double value) {
		params.minEscore = value;
		params.useMinEscore = true;
	}

}
