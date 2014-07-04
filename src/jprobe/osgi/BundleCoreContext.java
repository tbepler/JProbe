package jprobe.osgi;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.save.LoadException;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.Workspace;
import jprobe.services.data.Data;
import jprobe.services.data.ReadException;
import jprobe.services.data.WriteException;
import jprobe.services.function.Function;

public class BundleCoreContext implements JProbeCore{
	
	private static Logger LOG = LoggerFactory.getLogger(BundleCoreContext.class);
	
	private final Bundle m_Bundle;
	private final JProbeCore m_Parent;
	
	public BundleCoreContext(Bundle bundle, JProbeCore parent){
		m_Bundle = bundle;
		m_Parent = parent;
	}
	
	@Override
	public Mode getMode() {
		return m_Parent.getMode();
	}

	@Override
	public String getName() {
		return m_Parent.getName();
	}

	@Override
	public String getVersion() {
		return m_Parent.getVersion();
	}

	@Override
	public String getPreferencesDir() {
		return m_Parent.getPreferencesDir();
	}

	@Override
	public String getLogsDir() {
		return m_Parent.getLogsDir();
	}

	@Override
	public String getUserDir() {
		return m_Parent.getUserDir();
	}

	@Override
	public void addCoreListener(CoreListener listener) {
		m_Parent.addCoreListener(listener);
	}

	@Override
	public void removeCoreListener(CoreListener listener) {
		m_Parent.removeCoreListener(listener);
	}

	@Override
	public void shutdown() {
		LOG.info("Shutdown called by bundle: {}", m_Bundle);
		m_Parent.shutdown();
	}

	@Override
	public Workspace newWorkspace() {
		LOG.info("New workspace called by bundle: {}", m_Bundle);
		//return m_Parent.newWorkspace();
	}

	@Override
	public Workspace openWorkspace(InputStream in, String source) throws LoadException {
		LOG.info("Open workspace from source: {} called by bundle: {}", source, m_Bundle );
		return m_Parent.openWorkspace(in, source);
	}

	@Override
	public Workspace getWorkspace(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(Workspace w) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Workspace> getWorkspaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeWorkspace(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeWorkspace(Workspace w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int numWorkspaces() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<Function<?>> getFunctions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends Data>> getReadableDataClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileFilter> getReadFormats(Class<? extends Data> readClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <D extends Data> D readData(Class<D> readClass, FileFilter format,
			InputStream in) throws ReadException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReadable(Class<? extends Data> dataClass) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Class<? extends Data>> getWritableDataClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileNameExtensionFilter> getWriteFormats(
			Class<? extends Data> writeClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeData(Data d, FileNameExtensionFilter format,
			OutputStream out) throws WriteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isWritable(Class<? extends Data> dataClass) {
		// TODO Auto-generated method stub
		return false;
	}

}
