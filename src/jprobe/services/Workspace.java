package jprobe.services;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;

import org.osgi.framework.Bundle;

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
	 * Adds the given {@link Data} object 
	 * @param data
	 * @param name
	 */
	public void addData(Data data, String name);
	public void addData(Data data);
	public void removeData(Data data);
	public void removeData(String name);
	
	public void rename(Data data, String newName);
	public void rename(String oldName, String newName);
	
	public List<Data> getAllData();
	public List<String> getDataNames();
	
	public String getName(Data data);
	public Data getData(String name);
	
	public void clear();
	
	public boolean contains(String name);
	public boolean contains(Data data);
	
}
