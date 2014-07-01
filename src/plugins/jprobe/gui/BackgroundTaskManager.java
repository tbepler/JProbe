package plugins.jprobe.gui;

import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import plugins.jprobe.gui.services.Notification;
import util.Observer;
import util.Subject;
import util.save.ImportException;
import util.save.LoadException;
import util.save.SaveException;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.Workspace;
import jprobe.services.data.Data;
import jprobe.services.data.ReadException;
import jprobe.services.data.WriteException;

public class BackgroundTaskManager implements Subject<Notification>{
	
	private final Collection<Observer<Notification>> m_Obs = new HashSet<Observer<Notification>>();
	
	private final JFileChooser m_DataChooser = new JFileChooser();
	private final JFileChooser m_WorkspaceChooser = new JFileChooser();
	
	private final Frame m_Parent;
	private final ExecutorService m_Threads;
	
	private File m_SaveFile = null;
	
	public BackgroundTaskManager(Frame parent, ExecutorService threadPool){
		m_Parent = parent;
		m_Threads = threadPool;
		m_WorkspaceChooser.setFileFilter(Constants.SAVE_FILE_FILTER);
	}
	
	public void newWorkspace(final JProbeCore core){
		m_Threads.submit(new Runnable(){

			@Override
			public void run() {
				core.newWorkspace();
			}
			
		});
	}
	
	public static boolean stringEndsWithValidSaveExtension(String fileName){
		for(String ext : Constants.SAVE_FILE_EXTENSIONS){
			if(fileName.endsWith("."+ext)){
				return true;
			}
		}
		return false;
	}
	
	public void saveWorkspaceAs(final Workspace w) throws SaveException{
		int returnVal = m_WorkspaceChooser.showDialog(m_Parent, Constants.SAVE_APPROVE_TEXT);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File saveTo = m_WorkspaceChooser.getSelectedFile();
			String fileName = saveTo.toString();
			if(!stringEndsWithValidSaveExtension(fileName)){
				fileName += "." + Constants.SAVE_FILE_EXTENSIONS[0];
				saveTo = new File(fileName);
			}
			String target = getFilePath(saveTo);
			//don't save if the selected file has been saved previously, and there are no unsaved changes in the workspace
			if(!saveTo.equals(m_SaveFile) || w.unsavedChanges()){
				String prevName = w.getWorkspaceName();
				try {
					//name the workspace after the save target file
					w.setWorkspaceName(m_SaveFile.getName());
					this.saveWorkspace(w, new BufferedOutputStream(new FileOutputStream(saveTo)), target);
					m_SaveFile = saveTo;
				} catch (FileNotFoundException e) {
					//revert the workspace name
					w.setWorkspaceName(prevName);
					throw new SaveException(e);
				}
			}
		}
	}
	
	public void saveWorkspace(final Workspace w) throws SaveException{
		if(m_SaveFile == null){
			this.saveWorkspaceAs(w);
		}else{
			if(w.unsavedChanges()){ //only save again if there are unsaved changes to save
				String target;
				try {
					target = m_SaveFile.getCanonicalPath();
				} catch (IOException e) {
					target = m_SaveFile.getAbsolutePath();
				}
				try {
					this.saveWorkspace(w, new BufferedOutputStream(new FileOutputStream(m_SaveFile)), target);
				} catch (FileNotFoundException e) {
					throw new SaveException(e);
				}
			}
		}

	}
	
	public void saveWorkspace(final Workspace w, final OutputStream out, final String target){
		
		m_Threads.submit(new Runnable(){

			@Override
			public void run() {
				try {
					w.saveTo(out, target);
				} catch (SaveException e) {
					ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
				}
			}
			
		});
	}
	
	public void openWorkspace(final JProbeCore core) throws LoadException{
		int returnVal = m_WorkspaceChooser.showDialog(m_Parent, Constants.OPEN_APPROVE_TEXT);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File open = m_WorkspaceChooser.getSelectedFile();
			String target = getFilePath(open);
			try {
				this.openWorkspace(core, new BufferedInputStream(new FileInputStream(open)), target);
			} catch (FileNotFoundException e) {
				throw new LoadException(e);
			}
		}
	}
	
	public void openWorkspace(final JProbeCore core, final InputStream in, final String target){
		m_Threads.submit(new Runnable(){

			@Override
			public void run() {
				Notification start = Notification.startEvent(Constants.OPEN_WORKSPACE_START + target);
				fireNotification(start);
				try {
					core.openWorkspace(in);
					fireNotification(Notification.endEvent(Constants.OPEN_WORKSPACE_SUCCESS + target, start));
				} catch (LoadException e) {
					ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
					fireNotification(Notification.endEvent(Constants.OPEN_WORKSPACE_ERROR + target, start));
				}
				
			}
			
		});
	}
	
	public void importWorkspace(final Workspace w) throws ImportException{
		int returnVal = m_WorkspaceChooser.showDialog(m_Parent, Constants.IMPORT_APPROVE_TEXT);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File open = m_WorkspaceChooser.getSelectedFile();
			String target = getFilePath(open);
			try {
				this.importWorkspace(w, new BufferedInputStream(new FileInputStream(open)), target);
			} catch (FileNotFoundException e) {
				throw new ImportException(e);
			}
		}
	}
	
	public void importWorkspace(final Workspace w, final InputStream in, final String target){
		m_Threads.submit(new Runnable(){

			@Override
			public void run() {
				try {
					w.importFrom(in, target);
				} catch (ImportException e) {
					ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
				}
			}
			
		});
	}
	
	public void importData(final Workspace w, final JProbeCore core, final Class<? extends Data> dataClass) throws ReadException{
		if(!core.isReadable(dataClass)){
			throw new ReadException("Data class "+dataClass+" not readable.");
		}
		m_DataChooser.resetChoosableFileFilters();
		m_DataChooser.setAcceptAllFileFilterUsed(false);
		for(FileFilter filter : core.getReadFormats(dataClass)){
			m_DataChooser.addChoosableFileFilter(filter);
		}
		int returnVal = m_DataChooser.showDialog(m_Parent, Constants.IMPORT_APPROVE_TEXT);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File open = m_DataChooser.getSelectedFile();
			FileFilter format = m_DataChooser.getFileFilter();
			String source = getFilePath(open);
			try {
				this.importData(w, core, dataClass, format, new BufferedInputStream(new FileInputStream(open)), source);
			} catch (FileNotFoundException e) {
				throw new ReadException(e);
			}
		}
	}
	
	public void importData(
			final Workspace w,
			final JProbeCore core,
			final Class<? extends Data> dataClass,
			final FileFilter format,
			final InputStream in,
			final String source){
		
		m_Threads.submit(new Runnable(){

			@Override
			public void run() {
				Notification start = Notification.startEvent(Constants.getImportDataStart(dataClass, source));
				fireNotification(start);
				try {
					Data imported = core.readData(dataClass, format, in);
					addDataToWorkspace(w, source, imported);
					fireNotification(Notification.endEvent(Constants.getImportDataSuccess(dataClass, source), start));
				} catch (ReadException e) {
					ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
					fireNotification(Notification.endEvent(Constants.getImportDataError(dataClass, source), start));
				}
			}
			
		});
	}
	
	protected static void addDataToWorkspace(final Workspace w, final String source, final Data imported) {
		//name the imported data after the source it was imported from and make sure there is not a name collision
		String dataName = source;
		int count = 0;
		synchronized(w){ //synchronize on the workspace to ensure that multiple import threads don't have a race on the addData function
			while(w.contains(dataName)){
				dataName = source + "(" + (++count) + ")";
			}
			w.addData(imported, dataName);
		}
	}
	
	public void exportData(final JProbeCore core, final Data d, final String dataName) throws WriteException{
		if(!core.isWritable(d.getClass())){
			throw new WriteException("Data class "+d.getClass()+" not writable.");
		}
		m_DataChooser.resetChoosableFileFilters();
		m_DataChooser.setAcceptAllFileFilterUsed(false);
		for(FileNameExtensionFilter filter : core.getWriteFormats(d.getClass())){
			m_DataChooser.addChoosableFileFilter(filter);
		}
		int returnVal = m_DataChooser.showDialog(m_Parent, Constants.EXPORT_APPROVE_TEXT);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File export = m_DataChooser.getSelectedFile();
			FileNameExtensionFilter format = (FileNameExtensionFilter) m_DataChooser.getFileFilter();
			String source = getFilePath(export);
			try {
				this.exportData(core, d, dataName, format, new BufferedOutputStream(new FileOutputStream(export)), source);
			} catch (FileNotFoundException e) {
				throw new WriteException(e);
			}
		}
	}

	protected static String getFilePath(File export) {
		String source;
		try {
			source = export.getCanonicalPath();
		} catch (IOException e) {
			source = export.getAbsolutePath();
		}
		return source;
	}
	
	public void exportData(
			final JProbeCore core,
			final Data d,
			final String dataName,
			final FileNameExtensionFilter format,
			final OutputStream out,
			final String target){
		
		m_Threads.submit(new Runnable(){

			@Override
			public void run() {
				Notification start = Notification.startEvent(Constants.getExportDataStart(dataName, target));
				fireNotification(start);
				try {
					core.writeData(d, format, out);
					fireNotification(Notification.endEvent(Constants.getExportDataSuccess(dataName, target), start));
				} catch (WriteException e) {
					ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
					fireNotification(Notification.endEvent(Constants.getExportDataError(dataName, target), start));
				}
			}
			
		});
		
	}
	
	protected void fireNotification(Notification n){
		synchronized(m_Obs){
			for(Observer<Notification> obs : m_Obs){
				obs.update(this, n);
			}
		}
	}

	@Override
	public void register(Observer<Notification> obs) {
		synchronized(m_Obs){
			m_Obs.add(obs);
		}
	}

	@Override
	public void unregister(Observer<Notification> obs) {
		synchronized(m_Obs){
			m_Obs.remove(obs);
		}
	}
}
