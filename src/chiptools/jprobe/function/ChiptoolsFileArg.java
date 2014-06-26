package chiptools.jprobe.function;

import java.awt.Frame;

import javax.swing.JFileChooser;

import chiptools.Constants;
import chiptools.Resources;
import chiptools.jprobe.ChiptoolsActivator;
import jprobe.services.function.FileArgument;
import jprobe.services.function.Function;

public abstract class ChiptoolsFileArg<P> extends FileArgument<P>{

	@SuppressWarnings("rawtypes")
	protected ChiptoolsFileArg(Class<? extends Function> funcClass, Class<? extends ChiptoolsFileArg> clazz, boolean optional) {
		super(
				Resources.getArgumentName(funcClass, clazz),
				Resources.getArgumentDescription(funcClass, clazz),
				Resources.getArgumentCategory(funcClass, clazz),
				Resources.getArgumentFlag(funcClass, clazz),
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
