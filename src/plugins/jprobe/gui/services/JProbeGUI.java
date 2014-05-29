 package plugins.jprobe.gui.services;

import java.awt.Frame;
import java.awt.GridBagConstraints;

import javax.swing.JComponent;
import javax.swing.JMenu;

import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

import org.osgi.framework.Bundle;

public interface JProbeGUI {
	
	public Frame getGUIFrame();
	
	public void addGUIListener(GUIListener listener);
	public void removeGUIListener(GUIListener listener);
	
	public void addHelpTab(JComponent component, String tabName, Bundle responsible);
	public void removeHelpTab(JComponent component, Bundle responsible);
	
	public void addPreferencesTab(PreferencesPanel component, String tabName, Bundle responsible);
	public void removePreferencesTab(PreferencesPanel component, Bundle responsible);
	
	public void addComponent(JComponent component, GridBagConstraints constrains, Bundle responsible);
	public void removeComponent(JComponent component, Bundle responsible);
	
	public void addDropdownMenu(JMenu menu, Bundle responsible);
	public void removeDropdownMenu(JMenu menu, Bundle responsible);
	
	public void save();
	public void load();
	
	public void write(Data d);
	public void read(Class<? extends Data> type);
	
	public JProbeCore getJProbeCore();
	
	
}
