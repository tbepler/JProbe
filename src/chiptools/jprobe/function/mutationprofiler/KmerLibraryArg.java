package chiptools.jprobe.function.mutationprofiler;

import java.io.File;

import jprobe.services.function.Function;
import chiptools.jprobe.function.ChiptoolsDirArg;

public class KmerLibraryArg extends ChiptoolsDirArg<MutationProfilerParams>{

	public KmerLibraryArg(Function<?> parent, boolean optional) {
		super(parent.getClass(), KmerLibraryArg.class, optional);
	}
	
	
	@Override
	public boolean isValid(File f){
		return f != null && (f.isDirectory() || f.canRead());
	}

	@Override
	protected void process(MutationProfilerParams params, File f) {
		params.kmerLibrary = f;
	}

}
