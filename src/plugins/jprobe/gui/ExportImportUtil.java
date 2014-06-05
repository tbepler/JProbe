package plugins.jprobe.gui;

import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.osgi.framework.Bundle;

import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;

public class ExportImportUtil {
	
	public static final String WILDCARD = "*";
	public static final String FILE_NAME_WITH_EXTENSION_REGEX = "^.*\\..+$";

	public static void importData(Class<? extends Data> type, JProbeCore core, JFileChooser importChooser, Frame parent){
		//retrieve the registered file extension filters
		DataReader reader = core.getDataManager().getDataReader(type);
		if(reader == null){
			ErrorHandler.getInstance().handleWarning("Data type \""+type+"\" not readable.", GUIActivator.getBundle());
			return;
		}
		FileFilter[] formats = reader.getValidReadFormats();
		//if there are none, then there is an error in the DataReader, so warn the user and return
		if(formats.length <= 0){
			ErrorHandler.getInstance().handleWarning("There are no readable formats for the data type: "+type.getSimpleName(), GUIActivator.getBundle());
			return;
		}
		//set the file chooser's file filters to those retrieved above
		importChooser.resetChoosableFileFilters();
		importChooser.setAcceptAllFileFilterUsed(false);
		for(FileFilter format : formats){
			importChooser.addChoosableFileFilter(format);
		}
		//show the file chooser and read data from the selected file using the selected file format
		int returnVal = importChooser.showDialog(parent, "Import");
		if(returnVal == JFileChooser.APPROVE_OPTION){
			FileFilter selectedFormat = importChooser.getFileFilter();
			File f = importChooser.getSelectedFile();
			importData(core, f, reader, selectedFormat, GUIActivator.getBundle());
		}
	}
	
	public static void importData( final JProbeCore core, final File f, final DataReader reader, final FileFilter format, final Bundle b){
		BackgroundThread.getInstance().invokeLater(new Runnable(){

			@Override
			public void run() {
				try{
					Data in = reader.read(format, new FileInputStream(f));
					core.getDataManager().addData(in, b);
				}catch(Exception e){
					ErrorHandler.getInstance().handleException(e, b);
				}
			}
			
		});
	}

	public static void exportData(Data data, JProbeCore core, JFileChooser exportChooser, Frame parent){
		//retreive file formats for this data object
		DataWriter writer = core.getDataManager().getDataWriter(data.getClass());
		if(writer == null){
			ErrorHandler.getInstance().handleWarning("Data type \""+data.getClass()+"\" not writable.", GUIActivator.getBundle());
			return;
		}
		FileFilter[] formats = writer.getValidWriteFormats();
		//if there are none, then there is an error in the data writer. warn the user and return
		if(formats.length <= 0){
			ErrorHandler.getInstance().handleWarning("There are no writable formats for the data type: "+data.getClass().getSimpleName(), GUIActivator.getBundle());
			return;
		}
		//set the file chooser's file filters to those retreived above
		exportChooser.resetChoosableFileFilters();
		exportChooser.setAcceptAllFileFilterUsed(false);
		for(FileFilter format : formats){
			exportChooser.addChoosableFileFilter(format);
		}
		//show the file chooser and write the data to the selected file using the selected file format
		int returnVal = exportChooser.showDialog(parent, "Export");
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = exportChooser.getSelectedFile();
			FileNameExtensionFilter format = (FileNameExtensionFilter) exportChooser.getFileFilter();
			if(!fileEndsInValidExtension(file, format)){
				file = new File(file.toString()+"."+format.getExtensions()[0]);
			}
			exportData(writer, file, data, format);
		}
	}
	
	public static void exportData(final DataWriter writer, final File f, final Data d, final FileNameExtensionFilter format){
		BackgroundThread.getInstance().invokeLater(new Runnable(){

			@Override
			public void run() {
				try{
					BufferedWriter out = new BufferedWriter(new FileWriter(f));
					writer.write(d, format, out);
					out.close();
				}catch(Exception e){
					ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
				}
			}
			
		});
	}
	
	public static boolean fileEndsInValidExtension(File file, FileNameExtensionFilter filter){
		for(String ext : filter.getExtensions()){
			if(file.toString().endsWith("."+ext) || (ext.equals(WILDCARD) && file.toString().matches(FILE_NAME_WITH_EXTENSION_REGEX))){
				return true;
			}
		}
		return false;
	}
	
	
}
