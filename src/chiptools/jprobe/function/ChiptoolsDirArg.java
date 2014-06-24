package chiptools.jprobe.function;

import java.io.File;

import javax.swing.JFileChooser;

import chiptools.Constants;

public abstract class ChiptoolsDirArg<P> extends ChiptoolsFileArg<P> {

	@SuppressWarnings("rawtypes")
	protected ChiptoolsDirArg(Class<? extends ChiptoolsDirArg> clazz, boolean optional) {
		super(clazz, optional);
	}
	
	@Override
	protected JFileChooser getJFileChooser(){
		return Constants.getChiptoolsDirChooser();
	}

	@Override
	protected boolean isValid(File f) {
		return f.isDirectory();
	}

}
