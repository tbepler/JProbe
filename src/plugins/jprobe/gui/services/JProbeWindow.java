package plugins.jprobe.gui.services;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

import javax.swing.JMenu;

import util.save.ImportException;
import util.save.LoadException;
import util.save.SaveException;
import jprobe.services.Workspace;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.ReadException;
import jprobe.services.data.WriteException;

public interface JProbeWindow {
	
	/**
	 * Adds the specified component to this window using the given GridBagConstraints.
	 * @param component
	 * @param gbc
	 */
	public <T extends Component & Disposable> void addComponent(T component, GridBagConstraints gbc);
	
	/**
	 * Removes the specified component from this window.
	 * @param c
	 */
	public void removeComponent(Component c);
	
	/**
	 * Adds the specified menu to the menu bar of this window.
	 * @param menu
	 */
	public <T extends JMenu & Disposable> void addMenu(T menu);
	
	/**
	 * Removes the specified menu from this window's menu bar.
	 * @param menu
	 */
	public void removeMenu(JMenu menu);
	
	/**
	 * Adds a tab the the preferences dialog window containing the given PreferencesPanel and using the specified name.
	 * @param panel
	 * @param tabName
	 */
	public void addPreferencesPanel(PreferencesPanel panel, String tabName);
	
	/**
	 * Removes the the preferences dialog window tab containing the specified panel.
	 * @param panel
	 */
	public void removePreferencesPanel(PreferencesPanel panel);
	
	/**
	 * Adds a tab to the help dialog window containing the given component and using the specified name.
	 * @param component
	 * @param tabName
	 */
	public <T extends Component & Disposable> void addHelpComponent(T component, String tabName);
	
	/**
	 * Remove the help dialog window tab containing the specified component.
	 * @param c
	 */
	public void removeHelpComponent(Component c);
	
	/**
	 * Opens a new {@link Workspace}. This task is executed on a background thread.
	 * @return a {@link Future} object that can be used to track the execution of this task
	 */
	public Future<?> newWorkspace();
	
	/**
	 * Saves the {@link Workspace} open in this window. Saves the Workspace to the most recent saved file
	 * or queries the user for a file to save to, if there is none. This task is executed on a background thread.
	 * @return a {@link Future} object that can be used to track the execution of this task, or null
	 * if the save was cancelled
	 * @throws SaveException
	 */
	public Future<?> saveWorkspace() throws SaveException;
	
	/**
	 * Saves the {@link Workspace} open in this window, prompting the user to select a save destination.
	 * This task is executed on a background thread.
	 * @return a {@link Future} object that can be used to track the execution of this task, or null
	 * if the save was cancelled
	 * @throws SaveException
	 */
	public Future<?> saveWorkspaceAs() throws SaveException;
	
	/**
	 * Saves the {@link Workspace} open in this window to the given OutputStream using the 
	 * specified name. This task is executed on a background thread.
	 * @param out
	 * @param name
	 * @return a {@link Future} object that can be used to track the execution of this task
	 * @throws SaveException
	 */
	public Future<?> saveWorkspace(OutputStream out, String name) throws SaveException;
	
	/**
	 * Opens a saved {@link Workspace}. Queries the user for the Workspace to open.
	 * This task is executed in a background thread.
	 * @return a {@link Future} object that can be used to track the execution of this task,
	 * or null if this task was cancelled
	 * @throws LoadException
	 */
	public Future<?> openWorkspace() throws LoadException;
	
	/**
	 * Opens a save {@link Workspace} from the given InputStream using the specified name.
	 * This task is executed in a background thread.
	 * @param in
	 * @param name
	 * @return a {@link Future} object that can be used to track the execution of this task
	 * @throws LoadException
	 */
	public Future<?> openWorkspace(InputStream in, String name) throws LoadException;
	
	/**
	 * Imports another {@link Workspace} into this window's Workspace. Queries the user for the Workspace
	 * to import. This task is executed in a background thread.
	 * @return a {@link Future} object that can be used to track the execution of this task,
	 * or null if this task was cancelled
	 * @throws ImportException
	 */
	public Future<?> importWorkspace() throws ImportException;
	
	/**
	 * Imports another {@link Workspace} from the given InputStream using the specified name
	 * into this window's Workspace. This task is executed on a background thread.
	 * @param in
	 * @param name
	 * @return a {@link Future} object that can be used to track the execution of this task
	 * @throws ImportException
	 */
	public Future<?> importWorkspace(InputStream in, String name) throws ImportException;
	
	/**
	 * Imports Data of the specified class into this window's {@link Workspace}. Prompts the user
	 * to select the import file. This task is executed on a background thread.
	 * @param dataClass
	 * @return a {@link Future} object that can be used to track the execution of this task
	 * @throws ReadException
	 */
	public Future<?> importData(Class<? extends Data> dataClass) throws ReadException;
	
	/**
	 * Exports the specified Data. Prompts the user to select a file the data should be exorted to.
	 * This task is executed on a background thread.
	 * @param d
	 * @return a {@link Future} object that can be used to track the execution of this task
	 * @throws WriteException
	 */
	public Future<?> exportData(Data d) throws WriteException;
	
	/**
	 * Attempts to close this window and its {@link Workspace}. Returns True if closing
	 * was successful, False if closing was cancelled.
	 * @return - True if closing was successful, False if closing was cancelled.
	 */
	public boolean close();
	
	/**
	 * Pushes a new notification to be displayed by this window.
	 * @param note
	 */
	public void pushNotification(Notification note);
	
	/**
	 * Reports the given exception to the user.
	 * @param e
	 */
	public void reportException(Exception e);
	
	/**
	 * Reports the given error message to the user.
	 * @param message
	 */
	public void reportError(String message);
	
	/**
	 * Reports the given warning message to the user.
	 * @param message
	 */
	public void reportWarning(String message);
	
	/**
	 * Returns the ancestor Frame of this window.
	 * @return
	 */
	public Frame getParentFrame();
	
	/**
	 * Returns this window's {@link Workspace}.
	 * @return
	 */
	public Workspace getWorkspace();
	
	/**
	 * Returns the {@link JProbeCore}.
	 * @return
	 */
	public JProbeCore getCore();
	
}
