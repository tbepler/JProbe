package plugins.dataviewer.gui;

import java.awt.Frame;

import javax.swing.JOptionPane;

import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataUtils {
	
	private static final String[] OPTIONS = new String[]{"Yes", "No"};
	
	public static boolean rename(Data data, String newName, JProbeCore core, Frame parent){
		if(core.getDataManager().contains(newName)){
			int result = JOptionPane.showOptionDialog(parent, "Warning:\nThere is already data "
					+ "with the name \""+newName+"\" stored.\nRenaming will overwrite that data."
							+ "\n\nAre you sure you want to proceed?", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
							null, OPTIONS, OPTIONS[1]);
			if(result != 0){
				return false;
			}
		}
		core.getDataManager().rename(data, newName, DataviewerActivator.getBundle());
		return true;
	}
	
	public static boolean delete(Data data, JProbeCore core, Frame parent){
		if(core.getDataManager().contains(data)){
			int result = JOptionPane.showOptionDialog(parent, "Warning:\nThis will permanently "
					+ "remove this data from memory.\n\nAre your sure you want to proceed?", "Warning",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, OPTIONS, OPTIONS[1]);
			if(result != 0){
				return false;
			}
			core.getDataManager().removeData(data, DataviewerActivator.getBundle());
		}
		return !core.getDataManager().contains(data);
	}
	
}
