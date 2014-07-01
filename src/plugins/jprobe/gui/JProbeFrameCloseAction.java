package plugins.jprobe.gui;

import javax.swing.JOptionPane;

import jprobe.services.Workspace;

public class JProbeFrameCloseAction {
	
	public boolean close(JProbeFrame frame){
		Workspace w = frame.getWorkspace();
		if(w.unsavedChanges()){
			int result = JOptionPane.showOptionDialog(
					frame,
					Constants.getUnsavedChangesMessage(w.getWorkspaceName()),
					Constants.getUnsavedChangesTitle(w.getWorkspaceName()),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,
					null,
					null
					);
			switch(result){
			case JOptionPane.CANCEL_OPTION:
				return false;
			case JOptionPane.CLOSED_OPTION:
				return false;
			case JOptionPane.YES_OPTION:
				return save(core, parent);
			default:
				break;
			}
		}
		return true;
	}
	
}
