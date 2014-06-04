package plugins.jprobe.gui;

import java.awt.Event;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.osgi.framework.Bundle;

import plugins.jprobe.gui.filemenu.FileMenu;
import plugins.jprobe.gui.notification.NotificationPanel;
import plugins.jprobe.gui.services.GUIEvent;
import plugins.jprobe.gui.services.GUIListener;
import plugins.jprobe.gui.services.JProbeGUI;
import plugins.jprobe.gui.services.PreferencesPanel;
import plugins.jprobe.gui.utils.DialogueMenu;
import plugins.jprobe.gui.utils.TabDialogueWindow;
import util.gui.SwingUtils;
import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.Log;
import jprobe.services.data.Data;

public class JProbeGUIFrame extends JFrame implements JProbeGUI{
	private static final long serialVersionUID = 1L;
	
	private Bundle m_Bundle;
	private JProbeCore m_Core;
	private JPanel m_ContentPane;
	private JMenuBar m_MenuBar;
	private Queue<JMenu> m_PluginMenuItems;
	private PreferencesWindow m_PreferencesWindow;
	private TabDialogueWindow m_HelpWindow;
	private DialogueMenu m_PreferencesMenu;
	private DialogueMenu m_HelpMenu;
	private FileMenu m_FileMenu;
	private JFileChooser m_ImportChooser;
	private JFileChooser m_ExportChooser;
	private NotificationPanel m_NotePanel;
	private Collection<GUIListener> m_Listeners;
	
	JProbeGUIFrame(JProbeCore core, String name, Bundle bundle, GUIConfig config){
		super(name);
		m_ImportChooser = new JFileChooser();
		m_ExportChooser = new JFileChooser();
		this.m_Bundle = bundle;
		this.m_Core = core;
		m_PluginMenuItems = new PriorityQueue<JMenu>(10, new Comparator<JMenu>(){
			@Override
			public int compare(JMenu arg0, JMenu arg1){
				return arg0.getText().compareTo(arg1.getText());
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
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent event){
				JProbeGUIFrame.this.quit();
			}
		});
		m_FileMenu = new FileMenu(this, m_Core);
		m_MenuBar.add(m_FileMenu);
		m_PreferencesWindow = new PreferencesWindow(this, "Preferences", true);
		m_HelpWindow = new TabDialogueWindow(this, "Help", true);
		m_PreferencesMenu = new DialogueMenu("Preferences", m_PreferencesWindow);
		m_PreferencesMenu.setMnemonic(KeyEvent.VK_P);
		m_PreferencesMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
		m_MenuBar.add(m_PreferencesMenu);
		m_HelpMenu = new DialogueMenu("Help", m_HelpWindow);
		m_HelpMenu.setMnemonic(KeyEvent.VK_H);
		m_HelpMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK));
		m_MenuBar.add(m_HelpMenu);
		
		m_NotePanel = initNotificationPanel(m_Core);
		
		this.pack();
		this.setSize(config.getDimension());
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int x;
		if(config.getX() < 0 || !SwingUtils.isXOnScreen(config.getX())){
			x = center.x-this.getWidth()/2;
		}else{
			x = config.getX();
		}
		int y;
		if(config.getY() < 0 || !SwingUtils.isYOnScreen(config.getY())){
			y = center.y-this.getHeight()/2;
		}else{
			y = config.getY();
		}
		this.setLocation(x,y);
		this.setExtendedState(config.getExtendedState());
	}
	
	protected NotificationPanel initNotificationPanel(JProbeCore core){
		NotificationPanel panel = new NotificationPanel(core);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridy = 100;
		this.add(panel, gbc);
		return panel;
	}
	
	public void quit(){
		if(JOptionPane.showConfirmDialog(JProbeGUIFrame.this, "Exit JProbe?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION){
			if(SaveLoadUtil.unsavedWorkspaceCheck(m_Core, this) == SaveLoadUtil.PROCEED){
				JProbeGUIFrame.this.m_Core.shutdown();
			}
		}
	}
	
	@Override
	public void dispose(){
		m_NotePanel.dispose();
		super.dispose();
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
	public void addHelpTab(JComponent component, String tabName, Bundle responsible) {
		m_HelpWindow.addTab(component, tabName);
		checkDebugAndLog("Help tab "+tabName+" added by plugin: "+responsible.getSymbolicName());
	}

	@Override
	public void removeHelpTab(JComponent component, Bundle responsible) {
		m_HelpWindow.removeTab(component);
		checkDebugAndLog("Help tab "+component.toString()+" removed by plugin: "+responsible.getSymbolicName());
	}

	@Override
	public void addPreferencesTab(PreferencesPanel component, String tabName, Bundle responsible) {
		m_PreferencesWindow.addTab(component, tabName);
		checkDebugAndLog("Preferences tab "+tabName+" added by plugin: "+responsible.getSymbolicName());
	}

	@Override
	public void removePreferencesTab(PreferencesPanel component, Bundle responsible) {
		m_PreferencesWindow.removeTab(component);
		checkDebugAndLog("Preferences tab "+component.toString()+" removed by plugin: "+responsible.getSymbolicName());
	}
	
	public JFileChooser getExportChooser(){
		return m_ExportChooser;
	}
	
	public JFileChooser getImportChooser(){
		return m_ImportChooser;
	}

	@Override
	public void save() {
		SaveLoadUtil.save(m_Core, this);
	}

	@Override
	public void load() {
		SaveLoadUtil.load(m_Core, this);
	}

	@Override
	public void write(Data d) {
		ExportImportUtil.exportData(d, m_Core, m_ExportChooser, this);
	}

	@Override
	public void read(Class<? extends Data> type) {
		ExportImportUtil.importData(type, m_Core, m_ImportChooser, this);
	}
	
}
