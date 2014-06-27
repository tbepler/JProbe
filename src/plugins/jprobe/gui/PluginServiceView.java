package plugins.jprobe.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JMenu;

import org.osgi.framework.Bundle;

import jprobe.services.DataManager;
import jprobe.services.JProbeCore;
import plugins.jprobe.gui.services.Disposable;
import plugins.jprobe.gui.services.JProbeWindow;
import plugins.jprobe.gui.services.PreferencesPanel;

public class PluginServiceView implements JProbeWindow{
	
	private final Collection<Component> m_Comps = new ArrayList<Component>();
	private final Collection<JMenu> m_Menues = new ArrayList<JMenu>();
	private final Collection<PreferencesPanel> m_PrefsPanels = new ArrayList<PreferencesPanel>();
	private final Collection<Component> m_HelpComps = new ArrayList<Component>();
	private final Collection<Disposable> m_Disposables = new ArrayList<Disposable>();
	private final JProbeGUIFrame m_Frame;
	private final Bundle m_Bundle;
	
	public PluginServiceView(JProbeGUIFrame frame, Bundle bundle){
		m_Frame = frame;
		m_Bundle = bundle;
	}
	
	@Override
	public <T extends Component & Disposable> void addComponent(T component, GridBagConstraints gbc) {
		m_Comps.add(component);
		m_Disposables.add(component);
		m_Frame.addComponent(component, gbc, m_Bundle);
	}

	@Override
	public <T extends JMenu & Disposable> void addMenu(T menu) {
		m_Menues.add(menu);
		m_Disposables.add(menu);
		m_Frame.addMenu(menu, m_Bundle);
	}

	@Override
	public JFrame getFrame() {
		return m_Frame;
	}

	@Override
	public DataManager getWorkspace() {
		return m_Frame.getJProbeCore().getDataManager();
	}

	@Override
	public JProbeCore getCore() {
		return m_Frame.getJProbeCore();
	}

	@Override
	public void addPreferencesPanel(PreferencesPanel panel, String tabName) {
		m_PrefsPanels.add(panel);
		m_Disposables.add(panel);
		m_Frame.addPreferencesTab(panel, tabName, m_Bundle);
	}

	@Override
	public <T extends Component & Disposable> void addHelpComponent(T component, String tabName) {
		m_HelpComps.add(component);
		m_Disposables.add(component);
		m_Frame.addHelpTab(component, tabName, m_Bundle);
	}
	
	
	public void removeAll(){
		this.removeComponents();
		this.removeMenues();
		this.removePreferencesPanels();
		this.removeHelpComponents();
		this.emtpyDisposables();
	}

	private void emtpyDisposables() {
		for(Disposable d : m_Disposables){
			d.dispose();
		}
		m_Disposables.clear();
	}

	private void removeHelpComponents() {
		for(Component c : m_HelpComps){
			m_Frame.removeHelpTab(c, m_Bundle);
		}
		m_HelpComps.clear();
	}

	private void removePreferencesPanels() {
		for(PreferencesPanel p : m_PrefsPanels){
			m_Frame.removePreferencesTab(p, m_Bundle);
		}
		m_PrefsPanels.clear();
	}

	private void removeMenues() {
		for(JMenu m : m_Menues){
			m_Frame.removeMenu(m, m_Bundle);
		}
		m_Menues.clear();
	}
	
	private void removeComponents(){
		for(Component c : m_Comps){
			m_Frame.removeComponent(c, m_Bundle);
		}
		m_Comps.clear();
	}

}
