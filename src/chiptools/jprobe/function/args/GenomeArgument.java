package chiptools.jprobe.function.args;

import java.io.File;

import chiptools.jprobe.function.ChiptoolsFileArg;
import chiptools.jprobe.function.params.GenomeParam;
import jprobe.services.function.Function;

public class GenomeArgument extends ChiptoolsFileArg<GenomeParam>{

	public GenomeArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				GenomeArgument.class,
				optional
				);
	}

	@Override
	protected boolean isValid(File f) {
		return f != null && f.canRead();
	}

	@Override
	protected void process(GenomeParam params, File f) {
		params.setGenomeFile(f);
	}

}
