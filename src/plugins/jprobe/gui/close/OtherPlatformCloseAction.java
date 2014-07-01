package plugins.jprobe.gui.close;

import plugins.jprobe.gui.GUIActivator;
import plugins.jprobe.gui.JProbeFrame;

public class OtherPlatformCloseAction extends CloseAction{


	public OtherPlatformCloseAction(GUIActivator activator) {
		super(activator);
	}
	
	@Override
	public boolean close(JProbeFrame frame){
		if(m_Activator.numFrames() == 1 && m_Activator.frameOpen(frame)){
			if(m_Activator.confirmExit() && this.checkCloseWorkspace(frame)){
				m_Activator.exit();
			}
			return false;
		}else{
			return super.close(frame);
		}
		
	}

	@Override
	protected void closeWorkspace(JProbeFrame frame) {
		frame.getCore().closeWorkspace(frame.getWorkspace());	
	}
	
	
}
