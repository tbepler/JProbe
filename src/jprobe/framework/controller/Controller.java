package jprobe.framework.controller;

import java.util.Collection;
import java.util.Properties;

import jprobe.framework.HelpPrinter;
import jprobe.framework.Stoppable;
import jprobe.framework.model.Model;
import util.progress.ProgressListener;

/**
 * The controller interface defines methods for creating new empty workspaces or opening workspaces from
 * sources. It also allows registering of {@link ControllerObserver}s for listening to new workspace and
 * workspace closed events.
 * 
 * @author Tristan Bepler
 *
 */
public interface Controller extends Stoppable{
	
	/**
	 * Starts this controller using the given {@link Model}, {@link Properties}, and {@link HelpPrinter}.
	 * The HelpPrinter can be used to print the launcher's help statement.
	 * @param model - Model that should be used by this controller
	 * @param props - Properties object from which settings can be read and to which settings should be
	 * stored, if desired
	 * @param systemHelp - HelpPrinter for printing the launcher's help statement
	 */
	public void start(Model model, Properties props, HelpPrinter systemHelp);
	
	/**
	 * Starts a new workspace.
	 */
	public void newWorkspace();
	
	/**
	 * Starts a new workspace, loading from the given {@link Source}. Load progress will be reported
	 * to the given {@link ProgressListener}.
	 * @param s - Source from which the workspace will be loaded.
	 * @param l - ProgressListener to which load progress will be reported
	 */
	public void openWorkspace(Source s, ProgressListener l);
	
	/**
	 * Returns a collection containing controllers for the opened workspaces.
	 * @return
	 */
	public Collection<WorkspaceController> getWorkspaces();
	
	/**
	 * Adds a {@link ControllerObserver} to this controller.
	 * @param obs
	 */
	public void addControllerObserver(ControllerObserver obs);
	
	/**
	 * Removes a {@link ControllerObserver} from this controller.
	 * @param obs
	 */
	public void removeControllerObserver(ControllerObserver obs);
	
}
