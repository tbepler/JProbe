package jprobe.services.data;

import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.table.TableModel;


/**
 * This interface defines Data objects that are stored by the JProbeCore and can be displayed using
 * the createTable() method in a GUI.
 * 
 * @author Tristan Bepler
 *
 */
public interface Data extends TableModel, Serializable{
	
	/**
	 * This method is called to create the JTable component for displaying this data object in the GUI.
	 * @return - JTable for displaying this data
	 */
	public JTable createTable();
	
	/**
	 * This method is called when this Data is being deleted from the JProbeCore. It should eliminate
	 * any dangling references, so that this object can be garbage collected.
	 */
	public void dispose();
	
}
