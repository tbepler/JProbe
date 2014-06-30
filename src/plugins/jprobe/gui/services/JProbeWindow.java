package plugins.jprobe.gui.services;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.io.InputStream;
import java.io.OutputStream;

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
	 * Adds the specified menu to the menu bar of this window.
	 * @param menu
	 */
	public <T extends JMenu & Disposable> void addMenu(T menu);
	
	/**
	 * Adds a tab the the preferences dialog window containing the given PreferencesPanel and using the specified name.
	 * @param panel
	 * @param tabName
	 */
	public void addPreferencesPanel(PreferencesPanel panel, String tabName);
	
	/**
	 * Adds a tab to the help dialog window containing the given component and using the specified name.
	 * @param component
	 * @param tabName
	 */
	public <T extends Component & Disposable> void addHelpComponent(T component, String tabName);
	
	/**
	 * Opens a new {@link Workspace}.
	 */
	public void newWorkspace();
	
	/**
	 * Saves the {@link Workspace} open in this window.
	 * @throws SaveException
	 */
	public void saveWorkspace() throws SaveException;
	
	/**
	 * Saves the {@link Workspace} open in this window to the given OutputStream using the 
	 * specified name.
	 * @param out
	 * @param name
	 * @throws SaveException
	 */
	public void saveWorkspace(OutputStream out, String name) throws SaveException;
	
	/**
	 * Opens a saved {@link Workspace}.
	 * @throws LoadException
	 */
	public void openWorkspace() throws LoadException;
	
	/**
	 * Opens a save {@link Workspace} from the given InputStream using the specified name.
	 * @param in
	 * @param name
	 * @throws LoadException
	 */
	public void openWorkspace(InputStream in, String name) throws LoadException;
	
	/**
	 * Imports another {@link Workspace} into this window's Workspace.
	 * @throws ImportException
	 */
	public void importWorkspace() throws ImportException;
	
	/**
	 * Imports another {@link Workspace} from the given InputStream using the specified name
	 * into this window's Workspace.
	 * @param in
	 * @param name
	 * @throws ImportException
	 */
	public void importWorkspace(InputStream in, String name) throws ImportException;
	
	/**
	 * Imports Data of the specified class into this window's {@link Workspace}.
	 * @param dataClass
	 * @throws ReadException
	 */
	public void importData(Class<? extends Data> dataClass) throws ReadException;
	
	/**
	 * Exports the specified Data.
	 * @param d
	 * @throws WriteException
	 */
	public void exportData(Data d) throws WriteException;
	
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
