package jprobe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.Workspace;

public class WorkspaceManager {
	
	private final Collection<CoreListener> m_Listeners = new HashSet<CoreListener>();
	
	private final List<Workspace> m_Workspaces = new ArrayList<Workspace>();
	private final JProbeCore m_Core;
	
	public WorkspaceManager(JProbeCore core){
		m_Core = core;
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
	
}
