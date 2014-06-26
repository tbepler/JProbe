package chiptools.jprobe.function.agilentformatter;

import jprobe.services.function.Function;
import chiptools.jprobe.function.ChiptoolsTextArg;

public class ArrayNameArgument extends ChiptoolsTextArg<AgilentFormatterParams> {

	public ArrayNameArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				ArrayNameArgument.class,
				"array_name",
				optional,
				"array_name"
				);
	}

	@Override
	protected boolean isValid(String s) {
		return s.length() <= 15;
	}

	@Override
	protected void process(AgilentFormatterParams params, String s) {
		params.ARRAY_NAME = s;
	}

}
