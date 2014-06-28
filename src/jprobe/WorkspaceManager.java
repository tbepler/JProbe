package jprobe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import jprobe.services.AbstractServiceListener;
import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.Workspace;

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
		Workspace w = new JProbeWorkspace(m_Context, this.defaultName());
		m_Workspaces.add(w);
		this.notifyListeners(new CoreEvent(Type.WORKSPACE_NEW, w));
		return w;
	}
	
	public Workspace getWorkspace(int index) {
		if(index >= 0 && index < m_Workspaces.size()){
			return m_Workspaces.get(index);
		}
		return null;
	}

	public int indexOf(Workspace w) {
		return m_Workspaces.indexOf(w);
	}

	public List<Workspace> getWorkspaces() {
		return Collections.unmodifiableList(m_Workspaces);
	}

	public void closeWorkspace(int index) {
		if(index >= 0 && index < m_Workspaces.size()){
			Workspace closing = m_Workspaces.get(index);
			m_Workspaces.remove(index);
			this.notifyListeners(new CoreEvent(Type.WORKSPACE_CLOSED, closing));
		}
	}

	public void closeWorkspace(Workspace w) {
		int index = this.indexOf(w);
		if(index >= 0 && index < m_Workspaces.size()){
			m_Workspaces.remove(index);
			this.notifyListeners(new CoreEvent(Type.WORKSPACE_CLOSED, w));
		}
	}

	public int numWorkspaces() {
		return m_Workspaces.size();
	}
	
	protected void notifyListeners(CoreEvent event){
		for(CoreListener l : m_Listeners){
			l.update(m_Core, event);
		}
	}
	
	public void addCoreListener(CoreListener l){
		m_Listeners.add(l);
	}
	
	public void removeCoreListener(CoreListener l){
		m_Listeners.remove(l);
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
