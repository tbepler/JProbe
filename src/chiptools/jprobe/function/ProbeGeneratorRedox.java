package chiptools.jprobe.function;

import java.util.Collection;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.progress.ProgressListener;
import chiptools.jprobe.function.params.ProbeGeneratorParams;

public class ProbeGeneratorRedox extends AbstractChiptoolsFunction<ProbeGeneratorParams>{

	protected ProbeGeneratorRedox(Class<ProbeGeneratorParams> paramsClass) {
		super(ProbeGeneratorParams.class);
	}

	@Override
	public Collection<Argument<? super ProbeGeneratorParams>> getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data execute(ProgressListener l, ProbeGeneratorParams params)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
