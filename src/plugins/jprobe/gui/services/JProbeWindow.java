package plugins.jprobe.gui.services;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JMenu;

import util.save.ImportException;
import util.save.LoadException;
import util.save.SaveException;
import jprobe.services.Workspace;
import jprobe.services.JProbeCore;

public interface JProbeWindow {
	
	public <T extends Component & Disposable> void addComponent(T component, GridBagConstraints gbc);
	public <T extends JMenu & Disposable> void addMenu(T menu);
	public void addPreferencesPanel(PreferencesPanel panel, String tabName);
	public <T extends Component & Disposable> void addHelpComponent(T component, String tabName);
	
	public void newWorkspace();
	
	public void saveWorkspace() throws SaveException;
	public void saveWorkspace(OutputStream out) throws SaveException;
	
	public void openWorkspace() throws LoadException;
	public void openWorkspace(InputStream in) throws LoadException;
	
	public void importWorkspace() throws ImportException;
	public void importWorkspace(InputStream in) throws ImportException;
	
	
	public Frame getParentFrame();
	public Workspace getWorkspace();
	public JProbeCore getCore();
	
}
