package plugins.jprobe.gui.close;

import plugins.jprobe.gui.GUIActivator;
import plugins.jprobe.gui.JProbeFrame;

public class MacPlatformCloseAction extends CloseAction{


	public MacPlatformCloseAction(GUIActivator activator) {
		super(activator);
	}

	@Override
	protected void closeWorkspace(JProbeFrame frame) {
		frame.getCore().closeWorkspace(frame.getWorkspace());
	}
	
	
}
