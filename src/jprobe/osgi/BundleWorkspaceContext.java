package jprobe.osgi;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.save.ImportException;
import util.save.LoadException;
import util.save.SaveException;
import util.save.Saveable;
import util.save.SaveableEvent;
import util.save.SaveableListener;
import jprobe.services.Workspace;
import jprobe.services.WorkspaceEvent;
import jprobe.services.WorkspaceListener;
import jprobe.services.data.Data;

public class BundleWorkspaceContext implements Workspace, WorkspaceListener, SaveableListener{
	
	private static final Logger LOG = LoggerFactory.getLogger(BundleWorkspaceContext.class);
	
	private final Collection<SaveableListener> m_SaveListeners = new ArrayList<SaveableListener>();
	private final Collection<WorkspaceListener> m_WorkspaceListeners = new ArrayList<WorkspaceListener>();
	
	private final Bundle m_Bundle;
	private final Workspace m_Parent;
	
	public BundleWorkspaceContext(Bundle bundle, Workspace parent){
		m_Bundle = bundle;
		m_Parent = parent;
	}
	
	@Override
	public boolean unsavedChanges() {
		return m_Parent.unsavedChanges();
	}

	@Override
	public long saveTo(OutputStream out, String outName) throws SaveException {
		LOG.info("Save workspace: {} to target: {} called by bundle: {}", m_Parent, outName, m_Bundle);
		return m_Parent.saveTo(out, outName);
	}

	@Override
	public void loadFrom(InputStream in, String sourceName) throws LoadException {
		LOG.info("Load on workspace: {} from source: {} called by bundle: {}", m_Parent, sourceName, m_Bundle);
		m_Parent.loadFrom(in, sourceName);
	}

	@Override
	public void importFrom(InputStream in, String sourceName) throws ImportException {
		LOG.info("Import into workspace: {} from source: {} called by bundle: {}", m_Parent, sourceName, m_Bundle);
		m_Parent.importFrom(in, sourceName);
	}

	@Override
	public void addSaveableListener(SaveableListener l) {
		if(m_SaveListeners.isEmpty()){
			m_Parent.addSaveableListener(this);
		}
		m_SaveListeners.add(l);
	}

	@Override
	public void removeSaveableListener(SaveableListener l) {
		m_SaveListeners.remove(l);
		if(m_SaveListeners.isEmpty()){
			m_Parent.removeSaveableListener(this);
		}
	}

	@Override
	public void addWorkspaceListener(WorkspaceListener listener) {
		if(m_WorkspaceListeners.isEmpty()){
			m_Parent.addWorkspaceListener(this);
		}
		m_WorkspaceListeners.add(listener);
	}

	@Override
	public void removeWorkspaceListener(WorkspaceListener listener) {
		m_WorkspaceListeners.remove(listener);
		if(m_WorkspaceListeners.isEmpty()){
			m_Parent.removeWorkspaceListener(this);
		}
	}

	@Override
	public void putSaveable(String tag, Saveable s) {
		LOG.info("Saveable: {} with tag: {} added to workspace: {} by bundle: {}", s, tag, m_Parent, m_Bundle);
		m_Parent.putSaveable(tag, s);
	}

	@Override
	public void removeSaveable(String tag, Saveable s) {
		LOG.info("Saveable: {} with tag: {} removed from workspace: {} by bundle: {}", s, tag, m_Parent, m_Bundle);
		m_Parent.removeSaveable(tag, s);
	}

	@Override
	public String getWorkspaceName() {
		return m_Parent.getWorkspaceName();
	}

	@Override
	public void setWorkspaceName(String name) {
		LOG.info("Workspace: {} name set to {} by bundle: {}", m_Parent, name, m_Bundle);
		m_Parent.setWorkspaceName(name);
	}

	@Override
	public boolean isEmpty() {
		return m_Parent.isEmpty();
	}

	@Override
	public void addData(Data data, String name) {
		LOG.info("{} with name: {} added to workspace: {} by bundle: {}", data != null ? data.getClass() : null, name, m_Parent, m_Bundle);
		m_Parent.addData(data, name);
	}

	@Override
	public void addData(Data data) {
		LOG.info("{} with default name added to workspace: {} by bundle: {}", data != null ? data.getClass() : null, m_Parent, m_Bundle);
		m_Parent.addData(data);
	}

	@Override
	public void removeData(Data data) {
		if(m_Parent.contains(data)){
			String name = m_Parent.getDataName(data);
			LOG.info("{} with name: {} removed from workspace: {} by bundle: {}", data != null ? data.getClass() : null, name, m_Parent, m_Bundle);
			m_Parent.removeData(data);
		}
	}

	@Override
	public void removeData(String name) {
		if(m_Parent.contains(name)){
			Data data = m_Parent.getData(name);
			LOG.info("{} with name: {} removed from workspace: {} by bundle: {}", data.getClass(), name, m_Parent, m_Bundle);
			m_Parent.removeData(name);
		}
	}

	@Override
	public void rename(Data data, String newName) {
		if(m_Parent.contains(data)){
			String name = m_Parent.getDataName(data);
			LOG.info("{} with name: {} renamed to: {} in workspace: {} by bundle: {}", data != null ? data.getClass() : null, name, newName, m_Parent, m_Bundle);
			m_Parent.rename(data, newName);
		}
	}

	@Override
	public void rename(String oldName, String newName) {
		if(m_Parent.contains(oldName)){
			Data data = m_Parent.getData(oldName);
			LOG.info("{} with name: {} renamed to: {} in workspace: {} by bundle: {}", data != null ? data.getClass() : null, oldName, newName, m_Parent, m_Bundle);
			m_Parent.rename(oldName, newName);
		}
	}

	@Override
	public List<Data> getAllData() {
		return m_Parent.getAllData();
	}

	@Override
	public Collection<String> getDataNames() {
		return m_Parent.getDataNames();
	}

	@Override
	public String getDataName(Data data) {
		return m_Parent.getDataName(data);
	}

	@Override
	public Data getData(String name) {
		return m_Parent.getData(name);
	}

	@Override
	public void clear() {
		LOG.info("Workspace: {} cleared by bundle: {}", m_Parent, m_Bundle);
		m_Parent.clear();
	}

	@Override
	public boolean contains(String name) {
		return m_Parent.contains(name);
	}

	@Override
	public boolean contains(Data data) {
		return m_Parent.contains(data);
	}
	
	protected void fireSaveableEvent(SaveableEvent event){
		for(SaveableListener l : m_SaveListeners){
			l.update(this, event);
		}
	}
	
	protected void fireWorkspaceEvent(WorkspaceEvent event){
		for(WorkspaceListener l: m_WorkspaceListeners){
			l.update(this, event);
		}
	}

	@Override
	public void update(Saveable s, SaveableEvent event) {
		if(s == m_Parent){
			this.fireSaveableEvent(event);
		}
	}

	@Override
	public void update(Workspace source, WorkspaceEvent event) {
		if(source == m_Parent){
			this.fireWorkspaceEvent(event);
		}
	}

}
