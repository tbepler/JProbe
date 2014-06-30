package plugins.jprobe.gui.services;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JMenu;

import jprobe.services.Workspace;
import jprobe.services.JProbeCore;

public interface JProbeWindow {
	
	public <T extends Component & Disposable> void addComponent(T component, GridBagConstraints gbc);
	public <T extends JMenu & Disposable> void addMenu(T menu);
	public void addPreferencesPanel(PreferencesPanel panel, String tabName);
	public <T extends Component & Disposable> void addHelpComponent(T component, String tabName);
	
	public void newWorkspace();
	
	public void saveWorkspace();
	public void saveWorkspace(OutputStream out);
	
	public void openWorkspace();
	public void openWorkspace(InputStream in);
	
	public void importWorkspace();
	public void importWorkspace(InputStream in);
	
	
	public Frame getParentFrame();
	public Workspace getWorkspace();
	public JProbeCore getCore();
	
}
