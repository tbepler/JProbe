package plugins.jprobe.gui.services;

import java.awt.Component;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JMenu;

import jprobe.services.Workspace;
import jprobe.services.JProbeCore;

public interface JProbeWindow {
	
	public <T extends Component & Disposable> void addComponent(T component, GridBagConstraints gbc);
	public <T extends JMenu & Disposable> void addMenu(T menu);
	public void addPreferencesPanel(PreferencesPanel panel, String tabName);
	public <T extends Component & Disposable> void addHelpComponent(T component, String tabName);
	
	public JFrame getFrame();
	public Workspace getWorkspace();
	public JProbeCore getCore();
	
}
