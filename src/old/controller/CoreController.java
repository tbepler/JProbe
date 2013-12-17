package old.controller;

import java.io.File;
import java.io.FileFilter;

import old.datatypes.DataType;
import old.view.data.DataListener;

/**
 * This interface defines methods the core requires to send events to the view
 * @author Tristan Bepler
 *
 */
public interface CoreController {
	
	/**
	 * This method is called by the core to select the arguments that should be passed to the module to run. A return value of null signals the core
	 * to cancel running the module.
	 * @param requiredArgs
	 * @param optionalArgs
	 * @return an array of DataTypes containing first the requiredArgs and then the optionalArgs. unspecified optionalArgs should be filled with null.
	 */
	public DataType[] selectArgs(Class<? extends DataType>[] requiredArgs, Class<? extends DataType>[] optionalArgs);
	
	/**
	 * Called by the core to signal changes in its contained data. The event code specifies what kind of change occurred.
	 * @param event
	 */
	public void update(int event);
	
}
