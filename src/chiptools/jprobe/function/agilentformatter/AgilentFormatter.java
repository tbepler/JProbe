package chiptools.jprobe.function.agilentformatter;

import java.util.ArrayList;
import java.util.Collection;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.progress.ProgressListener;
import chiptools.jprobe.function.AbstractChiptoolsFunction;

public class AgilentFormatter extends AbstractChiptoolsFunction<AgilentFormatterParams>{

	public AgilentFormatter() {
		super(AgilentFormatterParams.class);
	}

	@Override
	public Collection<Argument<? super AgilentFormatterParams>> getArguments() {
		Collection<Argument<? super AgilentFormatterParams>> args = new ArrayList<Argument<? super AgilentFormatterParams>>();
		args.add(new ProbeCategoriesArgument(false));
		args.add(new ArrayNameArgument(false));
		
		return args;
	}

	@Override
	public Data execute(ProgressListener l, AgilentFormatterParams params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
