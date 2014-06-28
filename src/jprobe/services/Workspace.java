package jprobe.services;

import java.util.Collection;
import java.util.List;

import jprobe.services.data.Data;
import util.save.Saveable;

public interface Workspace extends Saveable{
	
	/**
	 * Registers the given listener on this Workspace to receive {@link WorkspaceEvent}s.
	 * @param listener
	 */
	public void addWorkspaceListener(WorkspaceListener listener);
	
	/**
	 * Unregisters the listener from this workspace.
	 * @param listener
	 */
	public void removeWorkspaceListener(WorkspaceListener listener);
	
	/**
	 * Adds the given {@link Saveable} to this Workspace. Whenever this Workspaces saves,
	 * loads, or imports, all registered Saveables will as well. Saveables are mapped
	 * by their tags. If a Saveable is already registered by the given tag, then it
	 * will be removed.
	 * @param s
	 */
	public void putSaveable(String tag, Saveable s);
	
	/**
	 * Unregisters the given {@link Saveable}.
	 * @param s
	 */
	public void removeSaveable(String tag, Saveable s);
	
	/**
	 * Returns the name of this Workspace.
	 * @return
	 */
	public String getWorkspaceName();
	
	/**
	 * Sets the name of this Workspace.
	 * @param name - new name for this Workspace
	 */
	public void setWorkspaceName(String name);
	
	/**
	 * Adds the given {@link Data} object to this Workspace using the given name for the data.
	 * If this Workspace already contains a Data object with the specified name, then that
	 * Data object will be removed from the Workspace.
	 * @param data - data to be added to this Workspace
	 * @param name - name that will be assigned to the added Data object
	 */
	public void addData(Data data, String name);
	
	/**
	 * Adds the given {@link Data} object to this Workspace. The data will be assigned a 
	 * default name.
	 * @param data - data to be added to this Workspace
	 */
	public void addData(Data data);
	
	/**
	 * Removes the specified {@link Data} object from this Workspace, if this Workspace
	 * contains it.
	 * @param data
	 */
	public void removeData(Data data);
	
	/**
	 * Removes the {@link Data} object with the given name contained by this Workspace,
	 * if this Workspace contains a Data object with the given name.
	 * @param name
	 */
	public void removeData(String name);
	
	/**
	 * Renames the given {@link Data} object to the specified name, if that object is contained in this Workspace.
	 * If there is already a Data object in this Workspace with the new name, then
	 * that object will be removed.
	 * @param data - Data object to rename
	 * @param newName - the new name of the Data object
	 */
	public void rename(Data data, String newName);
	
	/**
	 * Renames the {@link Data} object contained by this Workspace with the given oldName, assuming
	 * this Workspace contains an object with that name, to the given newName. If there is already
	 * an object contained in this Workspace with the new name, then that object will be removed.
	 * @param oldName - current name of Data to be renamed
	 * @param newName - the new name for the Data object
	 */
	public void rename(String oldName, String newName);
	
	/**
	 * Returns an unmodifiable list containing all the {@link Data} contained by this Workspace.
	 * @return
	 */
	public List<Data> getAllData();
	
	/**
	 * Returns a list containing the names of all the {@link Data} objects contained by
	 * this Workspace.
	 * @return
	 */
	public Collection<String> getDataNames();
	
	/**
	 * Returns the name assigned to the given {@link Data} object by this Workspace, if this Workspace
	 * contains the given Data. Returns null if this Workspace does not contain the given Data.
	 * @param data
	 * @return
	 */
	public String getDataName(Data data);
	
	/**
	 * Returns the {@link Data} object currently assigned the given name by this Workspace, or null if
	 * this Workspace contains the Data object with the given name.
	 * @param name
	 * @return
	 */
	public Data getData(String name);
	
	/**
	 * Clears this Workspace, removing all currently stored {@link Data} objects.
	 */
	public void clear();
	
	/**
	 * Returns True if this Workspace contains a {@link Data} object assigned the given name, False
	 * otherwise.
	 * @param name
	 * @return
	 */
	public boolean contains(String name);
	
	/**
	 * Returns True if this Workspace contains the given {@link Data} object, False otherwise.
	 * @param data
	 * @return
	 */
	public boolean contains(Data data);
	
}
