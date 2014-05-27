package chiptools.jprobe.function;

import java.util.Collection;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.progress.ProgressListener;
import chiptools.jprobe.function.params.BindingProfileParams;

public class BindingProfiler extends AbstractChiptoolsFunction<BindingProfileParams>{

	public BindingProfiler() {
		super(BindingProfileParams.class);
	}

	@Override
	public Collection<Argument<? super BindingProfileParams>> getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data execute(ProgressListener l, BindingProfileParams params)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
