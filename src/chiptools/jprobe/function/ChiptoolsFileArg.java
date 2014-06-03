package chiptools.jprobe.function;

import java.awt.Frame;
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
				optional,
				Constants.CHIPTOOLS_FILE_CHOOSER
				);
	}

	@Override
	protected Frame getParentFrame() {
		return ChiptoolsActivator.getGUIFrame();
	}

}
