package plugins.jprobe.gui;

import javax.swing.JFileChooser;

import jprobe.services.Workspace;
import jprobe.services.WorkspaceEvent;
import jprobe.services.WorkspaceEvent.Type;
import jprobe.services.WorkspaceListener;

public class BackgroundTaskManager implements WorkspaceListener{
	
	private final JFileChooser m_DataChooser = new JFileChooser();
	private final JFileChooser m_WorkspaceChooser = new JFileChooser();
	
	private final Workspace m_Workspace;
	private final BackgroundThread m_BackgroundThread;
	
	public BackgroundTaskManager(Workspace w){
		m_Workspace = w;
		m_BackgroundThread = new BackgroundThread(Constants.BACKGROUND_THREAD_NAME + "-" + m_Workspace.getWorkspaceName());
		m_BackgroundThread.start();
		m_Workspace.addWorkspaceListener(this);
	}

	@Override
	public void update(Workspace source, WorkspaceEvent event) {
		if(m_Workspace == source && event.type == Type.WORKSPACE_RENAMED){
			m_BackgroundThread.setName(Constants.BACKGROUND_THREAD_NAME + "-" + m_Workspace.getWorkspaceName());
		}
	}
}
