package chiptools.jprobe.function;

import java.awt.Frame;

import javax.swing.JFileChooser;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import jprobe.services.function.FileArgument;

public abstract class ChiptoolsFileArg<P> extends FileArgument<P>{

	@SuppressWarnings("rawtypes")
	protected ChiptoolsFileArg(Class<? extends ChiptoolsFileArg> clazz, boolean optional) {
		super(
				Constants.getName(clazz),
				Constants.getDescription(clazz),
				Constants.getCategory(clazz),
				Constants.getFlag(clazz),
				optional
				);
	}

	@Override
	protected JFileChooser getJFileChooser(){
		return Constants.getChiptoolsFileChooser();
	}
	
	@Override
	protected Frame getParentFrame() {
		return ChiptoolsActivator.getGUIFrame();
	}

}
