package chiptools.jprobe.function;

import java.util.ArrayList;
import java.util.Collection;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.progress.ProgressListener;
import chiptools.jprobe.function.params.ProbeGeneratorParams;
import chiptools.jprobe.function.args.*;

public class ProbeGeneratorRedox extends AbstractChiptoolsFunction<ProbeGeneratorParams>{

	protected ProbeGeneratorRedox(Class<ProbeGeneratorParams> paramsClass) {
		super(ProbeGeneratorParams.class);
	}

	@Override
	public Collection<Argument<? super ProbeGeneratorParams>> getArguments() {
		Collection<Argument<? super ProbeGeneratorParams>> args = new ArrayList<Argument<? super ProbeGeneratorParams>>();
		
		args.add(new PeakSeqsArgument(false));
		args.add(new KmerArgument(false));
		args.add(new PWMArgument(false));
		
		args.add(new ProbeLengthArgument(false));
		args.add(new BindingSiteArgument(false));
		args.add(new WindowSizeArgument(false));
		args.add(new EscoreArgument(false));
		
		return args;
	}

	@Override
	public Data execute(ProgressListener l, ProbeGeneratorParams params)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
