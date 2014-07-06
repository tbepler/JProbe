package jprobe.system.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import util.save.LoadException;
import jprobe.JProbeWorkspace;
import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.Workspace;
import jprobe.system.Constants;
import jprobe.system.osgi.services.AbstractServiceListener;

//extends AbstractServiceListener just for the BundleContext on load and unload methods
//makes things easy for getting the BundleContext from the JProbeActivator
public class WorkspaceManager extends AbstractServiceListener<Object> {
	
	private final Collection<CoreListener> m_Listeners = new HashSet<CoreListener>();
	
	private final List<Workspace> m_Workspaces = new ArrayList<Workspace>();
	private final JProbeCore m_Core;
	private BundleContext m_Context = null;
	
	public WorkspaceManager(JProbeCore core){
		super(Object.class);
		m_Core = core;
	}
	
	@Override
	public AbstractServiceListener<Object> load(BundleContext context){
		m_Context = context;
		return this;
	}
	
	@Override
	public AbstractServiceListener<Object> unload(BundleContext context){
		m_Context = context;
		return this;
	}
	
	private String defaultName(){
		String base = Constants.WORKSPACE_DEFAULT_NAME;
		String name = base;
		int count = 0;
		while(this.containsName(name)){
			name = base + "(" + (++count) + ")";
		}
		return name;
	}
	
	private boolean containsName(String name){
		for(Workspace w : m_Workspaces){
			if(w.getWorkspaceName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public Workspace newWorkspace() {
		Workspace w;
		synchronized(this){
			w = new JProbeWorkspace(m_Context, this.defaultName());
			m_Workspaces.add(w);
		}
		this.notifyListeners(new CoreEvent(Type.WORKSPACE_NEW, w));
		return w;
	}
	
	public Workspace openWorkspace(InputStream in, String source) throws LoadException{
		Workspace w = new JProbeWorkspace(m_Context, source);
		w.loadFrom(in, source);
		synchronized(this){
			m_Workspaces.add(w);
		}
		this.notifyListeners(new CoreEvent(Type.WORKSPACE_NEW, w));
		return w;
	}
	
	public synchronized Workspace getWorkspace(int index) {
		if(index >= 0 && index < m_Workspaces.size()){
			return m_Workspaces.get(index);
		}
		return null;
	}

	public synchronized int indexOf(Workspace w) {
		return m_Workspaces.indexOf(w);
	}

	public synchronized List<Workspace> getWorkspaces() {
		List<Workspace> copy = new ArrayList<Workspace>(m_Workspaces);
		return Collections.unmodifiableList(copy);
	}

	public void closeWorkspace(int index) {
		Workspace closing;
		synchronized(this){
			if(index >= 0 && index < m_Workspaces.size()){
				closing = m_Workspaces.get(index);
				m_Workspaces.remove(index);
			}else{
				closing = null;
			}
		}
		if(closing != null){
			this.notifyListeners(new CoreEvent(Type.WORKSPACE_CLOSED, closing));
		}
	}

	public void closeWorkspace(Workspace w) {
		Workspace removed = null;
		synchronized(this){
			int index = this.indexOf(w);
			if(index >= 0 && index < m_Workspaces.size()){
				removed = m_Workspaces.remove(index);
			}
		}
		if(removed != null){
			this.notifyListeners(new CoreEvent(Type.WORKSPACE_CLOSED, removed));
		}
		
	}

	public synchronized int numWorkspaces() {
		return m_Workspaces.size();
	}
	
	protected void notifyListeners(CoreEvent event){
		synchronized(m_Listeners){
			for(CoreListener l : m_Listeners){
				l.update(m_Core, event);
			}
		}
	}
	
	public void addCoreListener(CoreListener l){
		synchronized(m_Listeners){
			m_Listeners.add(l);
		}
	}
	
	public void removeCoreListener(CoreListener l){
		synchronized(m_Listeners){
			m_Listeners.remove(l);
		}
	}

	@Override
	public void register(Object service, Bundle provider) {
		//do nothing - sham method
	}

	@Override
	public void unregister(Object service, Bundle provider) {
		//do nothing - sham method
	}
	
}
