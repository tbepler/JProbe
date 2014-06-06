package chiptools.jprobe.function.mutationprofiler;

import java.io.File;
import chiptools.jprobe.function.ChiptoolsDirArg;

public class KmerLibraryArg extends ChiptoolsDirArg<MutationProfilerParams>{

	public KmerLibraryArg(boolean optional) {
		super(KmerLibraryArg.class, optional);
	}
	
	
	@Override
	public boolean isValid(File f){
		return f.isDirectory() || f.canRead();
	}

	@Override
	protected void process(MutationProfilerParams params, File f) {
		params.kmerLibrary = f;
	}

}
