package jprobe;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.BundleContext;

import util.WorkerThread;
import util.save.ImportException;
import util.save.LoadException;
import util.save.SaveException;
import util.save.Saveable;
import util.save.SaveableListener;
import jprobe.save.SaveManager;
import jprobe.services.Workspace;
import jprobe.services.WorkspaceListener;
import jprobe.services.data.Data;
import jprobe.system.model.DataManager;
import jprobe.system.model.WorkspaceEventThread;

public class JProbeWorkspace implements Workspace{
	
	private static final String DATABASE_TAG = "xXxDatabase9000";
	
	private final WorkerThread m_EventThread;
	private final DataManager m_Data;
	private final SaveManager m_Save;
	
	public JProbeWorkspace(BundleContext context, String name){
		m_EventThread = new WorkspaceEventThread();
		m_EventThread.start();
		m_Data = new DataManager(context, this, name, m_EventThread);
		m_Save = new SaveManager(this, m_EventThread);
		m_Save.addSaveable(m_Data, DATABASE_TAG);
	}
	
	@Override
	public String toString(){
		return m_Data.getWorkspaceName();
	}
	
	public void close() throws InterruptedException{
		m_EventThread.shutdown();
		m_EventThread.waitForShutdown();
	}
	
	@Override
	public boolean unsavedChanges() {
		return m_Save.unsavedChanges();
	}

	@Override
	public long saveTo(OutputStream out, String outName) throws SaveException{
		return m_Save.saveTo(out, outName);
	}

	@Override
	public void loadFrom(InputStream in, String sourceName) throws LoadException{
		m_Save.loadFrom(in, sourceName);
	}

	@Override
	public void importFrom(InputStream in, String sourceName) throws ImportException{
		m_Save.importFrom(in, sourceName);
	}

	@Override
	public void addSaveableListener(SaveableListener l) {
		m_Save.addSaveableListener(l);
	}

	@Override
	public void removeSaveableListener(SaveableListener l) {
		m_Save.removeSaveableListener(l);
	}

	@Override
	public void addWorkspaceListener(WorkspaceListener listener) {
		m_Data.addWorkspaceListener(listener);
	}

	@Override
	public void removeWorkspaceListener(WorkspaceListener listener) {
		m_Data.removeWorkspaceListener(listener);
	}

	@Override
	public String getWorkspaceName() {
		return m_Data.getWorkspaceName();
	}

	@Override
	public void setWorkspaceName(String name) {
		m_Data.setWorkspaceName(name);
	}

	@Override
	public void putSaveable(String tag, Saveable s) {
		m_Save.addSaveable(s, tag);
	}

	@Override
	public void removeSaveable(String tag, Saveable s) {
		m_Save.removeSaveable(s, tag);
	}

	@Override
	public void addData(Data data, String name) {
		if(data == null){
			throw new NullPointerException();
		}
		m_Data.addData(data, name);
	}

	@Override
	public void addData(Data data) {
		if(data == null){
			throw new NullPointerException();
		}
		m_Data.addData(data);
	}

	@Override
	public void removeData(Data data) {
		m_Data.removeData(data);
	}

	@Override
	public void removeData(String name) {
		m_Data.removeData(name);
	}

	@Override
	public void rename(Data data, String newName) {
		m_Data.rename(data, newName);
	}

	@Override
	public void rename(String oldName, String newName) {
		m_Data.rename(oldName, newName);
	}

	@Override
	public List<Data> getAllData() {
		return m_Data.getAllData();
	}

	@Override
	public Collection<String> getDataNames() {
		return m_Data.getDataNames();
	}

	@Override
	public String getDataName(Data data) {
		return m_Data.getDataName(data);
	}

	@Override
	public Data getData(String name) {
		return m_Data.getData(name);
	}

	@Override
	public void clear() {
		m_Data.clear();
	}

	@Override
	public boolean contains(String name) {
		return m_Data.contains(name);
	}

	@Override
	public boolean contains(Data data) {
		return m_Data.contains(data);
	}

	@Override
	public boolean isEmpty() {
		return m_Data.isEmpty();
	}

}
