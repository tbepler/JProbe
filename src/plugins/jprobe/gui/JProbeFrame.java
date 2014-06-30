package plugins.jprobe.gui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.osgi.framework.Bundle;

import bepler.crossplatform.Platform;
import bepler.crossplatform.PreferencesHandler;
import bepler.crossplatform.QuitHandler;
import plugins.jprobe.gui.filemenu.FileMenu;
import plugins.jprobe.gui.notification.NotificationPanel;
import plugins.jprobe.gui.services.GUIEvent;
import plugins.jprobe.gui.services.GUIListener;
import plugins.jprobe.gui.services.JProbeGUI;
import plugins.jprobe.gui.services.PreferencesPanel;
import plugins.jprobe.gui.utils.DialogueMenu;
import plugins.jprobe.gui.utils.TabDialogueWindow;
import util.gui.SortedJMenuBar;
import util.gui.SwingUtils;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.LoadEvent;
import jprobe.services.LoadListener;
import jprobe.services.JProbeLog;
import jprobe.services.SaveEvent;
import jprobe.services.SaveListener;
import jprobe.services.Workspace;
import jprobe.services.data.Data;

public class JProbeFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	protected class MenuComparator implements Comparator<JMenu>{

		@Override
		public int compare(JMenu m1, JMenu m2) {
			if(m1 == m2) return 0;
			if(m1 == null) return 1;
			if(m2 == null) return -1;
			if(m1 == m_FileMenu) return -1;
			if(m2 == m_FileMenu) return 1;
			if(m1 == m_HelpMenu) return 1;
			if(m2 == m_HelpMenu) return -1;
			if(m1 == m_PreferencesMenu) return 1;
			if(m2 == m_PreferencesMenu) return -1;
			return m1.getText().compareTo(m2.getText());
		}
		
	}
	
	private static JMenu createPreferencesMenu(String name, final JDialog dialog) {
		if(!Platform.getInstance().setPreferencesHandler(new PreferencesHandler(){

			@Override
			public void preferences() {
				dialog.setVisible(true);
			}
			
		})){
			//Platform does not support a system preferences event, so add a custom preferences menu to the menu bar
			JMenu prefsMenu = new DialogueMenu(name, dialog);
			prefsMenu.setMnemonic(KeyEvent.VK_P);
			prefsMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			return prefsMenu;
		}else{
			//the platform supports the preferences event, so set the preferences menu to null
			return null;
		}
		
	}
	
	private static JMenu createHelpMenu(String name, final JDialog dialog){
		JMenu helpMenu = new DialogueMenu(name, dialog);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		return helpMenu;
	}
	
	private final JFileChooser m_ImportChooser = new JFileChooser();
	private final JFileChooser m_ExportChooser = new JFileChooser();
	
	private final JPanel m_ContentPane = new JPanel(new GridBagLayout());
	private final JMenuBar m_MenuBar = new SortedJMenuBar(new MenuComparator());

	private final PreferencesWindow m_PreferencesWindow = new PreferencesWindow(this, Constants.PREFERENCES_NAME, true);
	private final JMenu m_PreferencesMenu = createPreferencesMenu(Constants.PREFERENCES_NAME, m_PreferencesWindow);
	
	private final TabDialogueWindow m_HelpWindow = new TabDialogueWindow(this, Constants.HELP_NAME, true);
	private final JMenu m_HelpMenu = createHelpMenu(Constants.HELP_NAME, m_HelpWindow);
	
	private final JProbeCore m_Core;
	private final FileMenu m_FileMenu;
	
	
	private NotificationPanel m_NotePanel;
	
	public JProbeFrame(JProbeCore core, Workspace w, GUIConfig config){
		super();
		m_Core = core;
		
		m_ContentPane.setOpaque(true);
		this.setJMenuBar(m_MenuBar);
		this.setContentPane(m_ContentPane);
		m_MenuBar.setVisible(true);
		m_ContentPane.setVisible(true);
		
		initCloseOperation();
		
		m_FileMenu = new FileMenu(this, m_Core);
		m_MenuBar.add(m_FileMenu);
		
		m_PreferencesWindow = new PreferencesWindow(this, "Preferences", true);
		initPreferencesMenu();
		
		m_HelpWindow = new TabDialogueWindow(this, "Help", true);
		m_HelpMenu = new DialogueMenu("Help", m_HelpWindow);
		m_HelpMenu.setMnemonic(KeyEvent.VK_H);
		m_HelpMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		m_MenuBar.add(m_HelpMenu);
		
		m_NotePanel = initNotificationPanel(m_Core);
		
		this.setModified(false);
		this.pack();
		initSizeAndLocation(config);
	}



	private void initSizeAndLocation(GUIConfig config) {
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

	private void initCloseOperation() {
		//override window closing event to make sure Felix is shutdown correctly
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent event){
				JProbeFrame.this.quit();
			}
		});
		//set Platform QuitHandler to call this.quit()
		Platform.getInstance().setQuitHandler(new QuitHandler(){

			@Override
			public boolean quit() {
				JProbeFrame.this.quit();
				//always return false to guarantee that the core has time to close all the plugins
				return false;
			}
			
		});
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
	
	public boolean quit(){
		if(JOptionPane.showConfirmDialog(JProbeFrame.this, "Exit JProbe?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION){
			if(SaveLoadUtil.unsavedWorkspaceCheck(m_Core, this) == SaveLoadUtil.PROCEED){
				JProbeFrame.this.m_Core.shutdown();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void dispose(){
		m_Core.getDataManager().removeListener(this);
		m_Core.unregisterLoad(this);
		m_Core.unregisterSave(this);
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
			JProbeLog.getInstance().write(m_Bundle, message);
		}
	}
	
	@Override
	public JProbeCore getJProbeCore(){
		return m_Core;
	}
	
	public void addComponent(Component comp, GridBagConstraints c, Bundle responsible){
		m_ContentPane.add(comp, c);
		this.invalidate();
		this.validate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.COMPONENT_ADDED, responsible));
	}
	
	public void removeComponent(Component comp, Bundle responsible){
		m_ContentPane.remove(comp);
		m_ContentPane.invalidate();
		m_ContentPane.validate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.COMPONENT_REMOVED, responsible));
	}
	
	public void addMenu(JMenu menu, Bundle responsible){
		m_PluginMenuItems.add(menu);
		m_MenuBar.removeAll();
		m_MenuBar.add(m_FileMenu);
		for(JMenu m : m_PluginMenuItems){
			m_MenuBar.add(m);
		}
		if(m_PreferencesMenu != null){
			m_MenuBar.add(m_PreferencesMenu);
		}
		m_MenuBar.add(m_HelpMenu);
		m_MenuBar.invalidate();
		m_MenuBar.validate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.MENU_ADDED, responsible));
	}
	
	public void removeMenu(JMenu menu, Bundle responsible){
		m_PluginMenuItems.remove(menu);
		m_MenuBar.remove(menu);
		m_MenuBar.invalidate();
		m_MenuBar.validate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.MENU_REMOVED, responsible));
	}

	@Override
	public Frame getGUIFrame() {
		return this;
	}

	@Override
	public void addHelpTab(Component component, String tabName, Bundle responsible) {
		m_HelpWindow.addTab(component, tabName);
		checkDebugAndLog("Help tab "+tabName+" added by plugin: "+responsible.getSymbolicName());
	}

	@Override
	public void removeHelpTab(Component component, Bundle responsible) {
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
	
	protected void setModified(final boolean modified){
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				//set the Window.documentModified property for mac os
				getRootPane().putClientProperty("Window.documentModified", modified);
				//add a * to the title of the pane if modified is true
				if(modified){
					setTitle(m_Name + " - *" + SaveLoadUtil.getWorkspaceName());
				}else{
					setTitle(m_Name + " - " + SaveLoadUtil.getWorkspaceName());
				}
			}
			
		});

	}

	@Override
	public void update(CoreEvent event) {
		this.setModified(m_Core.getDataManager().changedSinceSave());
	}

	@Override
	public void update(LoadEvent e) {
		if(e.type == LoadEvent.Type.LOADED){
			this.setModified(false);
		}
	}

	@Override
	public void update(SaveEvent e) {
		if(e.type == SaveEvent.Type.SAVED){
			this.setModified(false);
		}
	}
	
}
