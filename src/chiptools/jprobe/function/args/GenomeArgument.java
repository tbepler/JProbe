package chiptools.jprobe.function.args;

import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.function.params.GenomeParam;
import jprobe.services.function.FileArgument;

public class GenomeArgument extends FileArgument<GenomeParam>{

	public GenomeArgument(boolean optional) {
		super(
				Constants.getName(GenomeArgument.class),
				Constants.getDescription(GenomeArgument.class),
				Constants.getCategory(GenomeArgument.class),
				Constants.getFlag(GenomeArgument.class),
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

	@Override
	protected Frame getParentFrame() {
		return ChiptoolsActivator.getGUIFrame();
	}

	@Override
	protected JFileChooser getJFileChooser() {
		return Constants.getChiptoolsFileChooser();
	}

}
