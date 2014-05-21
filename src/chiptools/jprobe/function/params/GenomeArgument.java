package chiptools.jprobe.function.params;

import java.io.File;

import javax.swing.JFileChooser;

import jprobe.services.function.FileArgument;

public class GenomeArgument extends FileArgument<GenomeParam>{

	protected GenomeArgument(String name, String tooltip, String category, boolean optional, JFileChooser fileChooser) {
		super(name, tooltip, category, optional, fileChooser);
		//TODO
	}

	@Override
	protected boolean isValid(File f) {
		return f.canRead();
	}

	@Override
	protected void process(GenomeParam params, File f) {
		params.setGenomeFile(f);
	}

}
