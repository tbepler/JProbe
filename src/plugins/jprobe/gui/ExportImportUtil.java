package plugins.jprobe.gui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class ExportImportUtil {
	
	public static void importData(Class<? extends Data> type, JProbeCore core, JFileChooser importChooser, Component parent){
		//retrieve the registered file extension filters
		FileNameExtensionFilter[] formats = core.getDataManager().getValidReadFormats(type);
		//if there are none, then there is an error in the DataReader, so warn the user and return
		if(formats.length <= 0){
			JOptionPane.showMessageDialog(parent, "Warning: there are no readable formats for this data type.", "Import Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		//set the file chooser's file filters to those retrieved above
		importChooser.resetChoosableFileFilters();
		importChooser.setAcceptAllFileFilterUsed(false);
		for(FileNameExtensionFilter format : formats){
			importChooser.addChoosableFileFilter(format);
		}
		//show the file chooser and read data from the selected file using the selected file format
		int returnVal = importChooser.showDialog(parent, "Import");
		if(returnVal == JFileChooser.APPROVE_OPTION){
			try {
				FileNameExtensionFilter selectedFormat = (FileNameExtensionFilter) importChooser.getFileFilter();
				core.getDataManager().readData(importChooser.getSelectedFile(), type, selectedFormat, GUIActivator.getBundle());
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
			}
		}
	}

	public static void exportData(Data data, JProbeCore core, JFileChooser exportChooser, Component parent){
		//retreive file formats for this data object
		FileNameExtensionFilter[] formats = core.getDataManager().getValidWriteFormats(data.getClass());
		//if there are none, then there is an error in the data writer. warn the user and return
		if(formats.length <= 0){
			JOptionPane.showMessageDialog(parent, "Warning: there are no writable formats for this data type.", "Export Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		//set the file chooser's file filters to those retreived above
		exportChooser.resetChoosableFileFilters();
		exportChooser.setAcceptAllFileFilterUsed(false);
		for(FileNameExtensionFilter format : formats){
			exportChooser.addChoosableFileFilter(format);
		}
		//show the file chooser and write the data to the selected file using the selected file format
		int returnVal = exportChooser.showDialog(parent, "Export");
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = exportChooser.getSelectedFile();
			try {
				FileNameExtensionFilter format = (FileNameExtensionFilter) exportChooser.getFileFilter();
				if(!fileEndsInValidExtension(file, format)){
					file = new File(file.toString()+"."+format.getExtensions()[0]);
				}
				core.getDataManager().writeData(file, data, format);
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
			}
		}
	}
	
	public static boolean fileEndsInValidExtension(File file, FileNameExtensionFilter filter){
		for(String ext : filter.getExtensions()){
			if(file.toString().endsWith("."+ext)){
				return true;
			}
		}
		return false;
	}
	
	
}
