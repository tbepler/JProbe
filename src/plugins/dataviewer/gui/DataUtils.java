package plugins.dataviewer.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataUtils {
	
	private static final String[] OPTIONS = new String[]{"Yes", "No"};
	
	public static boolean rename(Data data, String newName, JProbeCore core, Component parent){
		if(core.getDataManager().contains(newName)){
			int result = JOptionPane.showOptionDialog(parent, "Warning: there is already a data object "
					+ "with the name \""+newName+"\" stored. Renaming will overwrite that object."
							+ " Are you sure you want to proceed?", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
							null, OPTIONS, OPTIONS[1]);
			if(result != 0){
				return false;
			}
		}
		core.getDataManager().rename(data, newName, DataviewerActivator.getBundle());
		return true;
	}
	
}
