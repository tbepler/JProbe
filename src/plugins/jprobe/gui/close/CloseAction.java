package plugins.jprobe.gui.close;

import java.awt.Frame;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.JOptionPane;

import plugins.jprobe.gui.Constants;
import plugins.jprobe.gui.GUIActivator;
import plugins.jprobe.gui.JProbeFrame;
import util.save.SaveException;
import jprobe.services.ErrorHandler;
import jprobe.services.Workspace;

public abstract class CloseAction {
	
	protected final GUIActivator m_Activator;
	
	public CloseAction(GUIActivator activator){
		m_Activator = activator;
	}
	
	protected abstract void closeWorkspace(JProbeFrame frame);
	
	public boolean close(JProbeFrame frame){
		if(this.checkCloseWorkspace(frame)){
			this.closeWorkspace(frame);
			return true;
		}
		return false;
	}
	
	protected boolean checkCloseWorkspace(JProbeFrame frame){
		Workspace w = frame.getWorkspace();
		if(w.unsavedChanges()){
			int result = this.showUnsavedChangesDialog(frame, w);
			switch(result){
			case JOptionPane.CANCEL_OPTION:
				return false;
			case JOptionPane.CLOSED_OPTION:
				return false;
			case JOptionPane.YES_OPTION:
				return this.save(frame);
			default:
				break;
			}
		}
		return true;
	}
	
	protected int showUnsavedChangesDialog(Frame parent, Workspace w){
		return JOptionPane.showOptionDialog(
				parent,
				Constants.getUnsavedChangesMessage(w.getWorkspaceName()),
				Constants.getUnsavedChangesTitle(w.getWorkspaceName()),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE,
				null,
				null,
				null
				);
	}

	protected boolean save(JProbeFrame frame) {
		Future<?> fut;
		try {
			fut = frame.saveWorkspace();
		} catch (SaveException e) {
			ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
			return false;
		}
		if(fut != null){
			try {
				fut.get();
			} catch (InterruptedException e) {
				//do nothing
			} catch (ExecutionException e) {
				ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
				return false;
			}
			return true;
		}
		return false;
	}
	
}
