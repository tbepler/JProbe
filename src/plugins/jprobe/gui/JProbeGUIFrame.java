package plugins.jprobe.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.osgi.framework.Bundle;

import plugins.jprobe.gui.filemenu.FileMenu;
import plugins.jprobe.gui.services.GUIEvent;
import plugins.jprobe.gui.services.GUIListener;
import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.Log;

public class JProbeGUIFrame extends JFrame implements JProbeGUI{
	private static final long serialVersionUID = 1L;
	
	private Bundle m_Bundle;
	private JProbeCore m_Core;
	private JPanel m_ContentPane;
	private JMenuBar m_MenuBar;
	private Queue<JMenu> m_PluginMenuItems;
	private TabDialogueWindow m_PreferencesWindow;
	private TabDialogueWindow m_HelpWindow;
	private DialogueMenu m_PreferencesMenu;
	private DialogueMenu m_HelpMenu;
	private FileMenu m_FileMenu;
	private Collection<GUIListener> m_Listeners;
	
	JProbeGUIFrame(JProbeCore core, String name, Bundle bundle, GUIConfig config){
		super(name);
		this.m_Bundle = bundle;
		this.m_Core = core;
		m_PluginMenuItems = new PriorityQueue<JMenu>(10, new Comparator<JMenu>(){
			@Override
			public int compare(JMenu arg0, JMenu arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
		});
		m_Listeners = new HashSet<GUIListener>();
		m_MenuBar = new JMenuBar();
		m_ContentPane = new JPanel(new GridBagLayout());
		m_ContentPane.setOpaque(true);
		this.setJMenuBar(m_MenuBar);
		this.setContentPane(m_ContentPane);
		m_MenuBar.setVisible(true);
		m_ContentPane.setVisible(true);
		//override window closing event to make sure Felix is shutdown correctly
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent event){
				JProbeGUIFrame.this.quit();
			}
		});
		this.setPreferredSize(new Dimension(config.getWidth(), config.getHeight()));
		this.pack();
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		this.setLocation(center.x-this.getWidth()/2, center.y-this.getHeight()/2);
		m_FileMenu = new FileMenu(this, m_Core);
		m_MenuBar.add(m_FileMenu);
		m_PreferencesWindow = new TabDialogueWindow(this, "Preferences", true);
		m_HelpWindow = new TabDialogueWindow(this, "Help", true);
		m_PreferencesMenu = new DialogueMenu("Preferences", m_PreferencesWindow);
		m_MenuBar.add(m_PreferencesMenu);
		m_HelpMenu = new DialogueMenu("Help", m_HelpWindow);
		m_MenuBar.add(m_HelpMenu);
	}
	
	public void quit(){
		if(JOptionPane.showConfirmDialog(JProbeGUIFrame.this, "Exit JProbe?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION){
			JProbeGUIFrame.this.m_Core.shutdown();
		}
	}
	
	@Override
	public void addGUIListener(GUIListener listener){
		m_Listeners.add(listener);
	}
	
	@Override
	public void removeGUIListener(GUIListener listener){
		m_Listeners.remove(listener);
	}
	
	private void notifyListeners(GUIEvent event){
		for(GUIListener l : m_Listeners){
			l.update(event);
		}
	}
	
	private void checkDebugAndLog(String message){
		Debug debugLevel = Debug.getLevel();
		if(debugLevel == Debug.LOG || debugLevel == Debug.FULL){
			Log.getInstance().write(m_Bundle, message);
		}
	}
	
	@Override
	public JProbeCore getJProbeCore(){
		return m_Core;
	}
	
	public void addComponent(JComponent comp, GridBagConstraints c, Bundle responsible){
		m_ContentPane.add(comp, c);
		comp.revalidate();
		comp.repaint();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.COMPONENT_ADDED, responsible));
	}
	
	public void removeComponent(JComponent comp, Bundle responsible){
		m_ContentPane.remove(comp);
		m_ContentPane.revalidate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.COMPONENT_REMOVED, responsible));
	}
	
	public void addDropdownMenu(JMenu menu, Bundle responsible){
		m_PluginMenuItems.add(menu);
		m_MenuBar.removeAll();
		m_MenuBar.add(m_FileMenu);
		for(JMenu m : m_PluginMenuItems){
			m_MenuBar.add(m);
		}
		m_MenuBar.add(m_PreferencesMenu);
		m_MenuBar.add(m_HelpMenu);
		m_MenuBar.revalidate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.MENU_ADDED, responsible));
	}
	
	public void removeDropdownMenu(JMenu menu, Bundle responsible){
		m_PluginMenuItems.remove(menu);
		m_MenuBar.remove(menu);
		m_MenuBar.revalidate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.MENU_REMOVED, responsible));
	}

	@Override
	public Frame getGUIFrame() {
		return this;
	}

	@Override
	public void addHelpTab(JComponent component, Bundle responsible) {
		m_HelpWindow.addTab(component, responsible.getSymbolicName());
		checkDebugAndLog("Help tab "+component.toString()+" added by plugin: "+responsible.getSymbolicName());
	}

	@Override
	public void removeHelpTab(JComponent component, Bundle responsible) {
		m_HelpWindow.removeTab(component);
		checkDebugAndLog("Help tab "+component.toString()+" removed by plugin: "+responsible.getSymbolicName());
	}

	@Override
	public void addPreferencesTab(JComponent component, Bundle responsible) {
		m_PreferencesWindow.addTab(component, responsible.getSymbolicName());
		checkDebugAndLog("Preferences tab "+component.toString()+" added by plugin: "+responsible.getSymbolicName());
	}

	@Override
	public void removePreferencesTab(JComponent component, Bundle responsible) {
		m_PreferencesWindow.removeTab(component);
		checkDebugAndLog("Preferences tab "+component.toString()+" removed by plugin: "+responsible.getSymbolicName());
	}
	
}
