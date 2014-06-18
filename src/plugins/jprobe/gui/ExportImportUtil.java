package plugins.jprobe.gui;

import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.osgi.framework.Bundle;

import plugins.jprobe.gui.notification.ExportEvent;
import plugins.jprobe.gui.notification.ImportEvent;
import plugins.jprobe.gui.notification.ImportEvent.Type;
import util.Observer;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;

public class ExportImportUtil {
	
	public static final String WILDCARD = "*";
	public static final String FILE_NAME_WITH_EXTENSION_REGEX = "^.*\\..+$";
	
	private static final Collection<Observer<ExportEvent>> m_ExportObs = new HashSet<Observer<ExportEvent>>();
	private static final Collection<Observer<ImportEvent>> m_ImportObs = new HashSet<Observer<ImportEvent>>();
	
	public static void registerImportObs(Observer<ImportEvent> obs){
		synchronized(m_ImportObs){
			m_ImportObs.add(obs);
		}
	}
	
	public static void unregisterImportObs(Observer<ImportEvent> obs){
		synchronized(m_ImportObs){
			m_ImportObs.remove(obs);
		}
	}
	
	private static void notifyObservers(ImportEvent e){
		synchronized(m_ImportObs){
			for(Observer<ImportEvent> obs : m_ImportObs){
				obs.update(null, e);
			}
		}
	}
	
	public static void registerExportObs(Observer<ExportEvent> obs){
		synchronized(m_ExportObs){
			m_ExportObs.add(obs);
		}
	}
	
	public static void unregisterExportObs(Observer<ExportEvent> obs){
		synchronized(m_ExportObs){
			m_ExportObs.remove(obs);
		}
	}
	
	private static void notifyObservers(ExportEvent e){
		synchronized(m_ExportObs){
			for(Observer<ExportEvent> obs : m_ExportObs){
				obs.update(null, e);
			}
		}
	}

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
				notifyObservers(new ImportEvent(Type.IMPORTING, reader.getReadClass(), f));
				try{
					FileInputStream stream = new FileInputStream(f);
					Data in = reader.read(format, stream);
					core.getDataManager().addData(in, b);
					notifyObservers(new ImportEvent(Type.IMPORTED, reader.getReadClass(), f));
					stream.close();
				}catch(Exception e){
					notifyObservers(new ImportEvent(Type.FAILED, reader.getReadClass(), f));
					ErrorHandler.getInstance().handleException(e, b);
				}catch(Throwable t){
					notifyObservers(new ImportEvent(Type.FAILED, reader.getReadClass(), f));
					ErrorHandler.getInstance().handleException(new RuntimeException(t), b);
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
			exportData(core, writer, file, data, format);
		}
	}
	
	public static void exportData(final JProbeCore core, final DataWriter writer, final File f, final Data d, final FileNameExtensionFilter format){
		BackgroundThread.getInstance().invokeLater(new Runnable(){

			@Override
			public void run() {
				String dataName = core.getDataManager().getDataName(d);
				notifyObservers(new ExportEvent(ExportEvent.Type.EXPORTING, dataName, f));
				try{
					BufferedWriter out = new BufferedWriter(new FileWriter(f));
					writer.write(d, format, out);
					out.close();
					notifyObservers(new ExportEvent(ExportEvent.Type.EXPORTED, dataName, f));
				}catch(Exception e){
					notifyObservers(new ExportEvent(ExportEvent.Type.FAILED, dataName, f));
					ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
				}catch(Throwable t){
					notifyObservers(new ExportEvent(ExportEvent.Type.FAILED, dataName, f));
					ErrorHandler.getInstance().handleException(new RuntimeException(t), GUIActivator.getBundle());
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
