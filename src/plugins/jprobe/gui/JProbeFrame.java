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
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
import plugins.jprobe.gui.close.CloseAction;
import plugins.jprobe.gui.filemenu.FileMenu;
import plugins.jprobe.gui.notification.NotificationPanel;
import plugins.jprobe.gui.services.Disposable;
import plugins.jprobe.gui.services.JProbeWindow;
import plugins.jprobe.gui.services.Notification;
import plugins.jprobe.gui.services.PreferencesPanel;
import plugins.jprobe.gui.utils.DialogueMenu;
import plugins.jprobe.gui.utils.TabDialogueWindow;
import util.Observer;
import util.Subject;
import util.gui.SortedJMenuBar;
import util.gui.SwingUtils;
import util.save.ImportException;
import util.save.LoadException;
import util.save.SaveException;
import util.save.Saveable;
import util.save.SaveableEvent;
import util.save.SaveableListener;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.JProbeLog;
import jprobe.services.Workspace;
import jprobe.services.WorkspaceEvent;
import jprobe.services.WorkspaceListener;
import jprobe.services.data.Data;
import jprobe.services.data.ReadException;
import jprobe.services.data.WriteException;

public class JProbeFrame extends JFrame implements JProbeWindow, WorkspaceListener, SaveableListener, Observer<Notification>{
	private static final long serialVersionUID = 1L;
	
	private final NotificationProducer m_NoteProducer = new NotificationProducer();
	
	private final PreferencesWindow m_PreferencesWindow = new PreferencesWindow(this, Constants.PREFERENCES_NAME, true);
	private final TabDialogueWindow m_HelpWindow = new TabDialogueWindow(this, Constants.HELP_NAME, true);
	
	private final JProbeCore m_Core;
	private final Workspace m_Workspace;
	private final JPanel m_ContentPane;
	private final JMenuBar m_MenuBar;
	private final String m_Name;
	private final BackgroundTaskManager m_TaskManager;
	private final CloseAction m_CloseAction;
	
	private final NotificationPanel m_NotePanel;
	
	public JProbeFrame(
			String frameName,
			JProbeCore core,
			Workspace w,
			ExecutorService threadPool,
			CloseAction closeAction,
			GUIConfig config){
		
		super();
		m_Name = frameName;
		m_Core = core;
		m_Workspace = w;
		m_Workspace.addSaveableListener(this);
		m_Workspace.addWorkspaceListener(this);
		m_TaskManager = new BackgroundTaskManager(this, threadPool);
		m_TaskManager.register(this);
		m_CloseAction = closeAction;
		
		m_ContentPane = this.createContentPane();
		this.setContentPane(m_ContentPane);
		
		m_MenuBar = this.createMenuBar(m_PreferencesWindow, m_HelpWindow);
		this.setJMenuBar(m_MenuBar);
		
		this.initCloseOperation();
		
		m_NotePanel = initNotificationPanel();
		
		this.setModified(false);
		this.pack();
		this.initSizeAndLocation(config);
	}
	
	protected JPanel createContentPane(){
		JPanel pane = new JPanel(new GridBagLayout());
		pane.setOpaque(true);
		return pane;
	}
	
	protected JMenuBar createMenuBar(JDialog preferences, JDialog help){
		JMenuBar menuBar = new JProbeMenuBar(
				this.createPreferencesMenu(preferences),
				this.createHelpMenu(help),
				new FileMenu(this)
				);
		return menuBar;
	}

	protected JMenu createHelpMenu(final JDialog dialog){
		JMenu helpMenu = new DialogueMenu(Constants.HELP_NAME, dialog);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		return helpMenu;
	}
	
	protected JMenu createPreferencesMenu(final JDialog dialog) {
		if(!Platform.getInstance().setPreferencesHandler(new PreferencesHandler(){

			@Override
			public void preferences() {
				dialog.setVisible(true);
			}
			
		})){
			//Platform does not support a system preferences event, so add a custom preferences menu to the menu bar
			JMenu prefsMenu = new DialogueMenu(Constants.PREFERENCES_NAME, dialog);
			prefsMenu.setMnemonic(KeyEvent.VK_P);
			prefsMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			return prefsMenu;
		}else{
			//the platform supports the preferences event, so set the preferences menu to null
			return null;
		}
		
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
	
	public File getLastSaveFile(){
		return m_TaskManager.getLastSaveFile();
	}

	private void initCloseOperation() {
		//override window closing event to call the close() method
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent event){
				JProbeFrame.this.close();
			}
		});
		
	}
	
	protected NotificationPanel initNotificationPanel(){
		NotificationPanel panel = new NotificationPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridy = 100;
		this.add(panel, gbc);
		return panel;
	}
	
	@Override
	public void dispose(){
		super.dispose();
	}
	
	protected void setModified(boolean modified){
		//set the Window.documentModified property for mac os
		getRootPane().putClientProperty("Window.documentModified", modified);
		//add a * to the title of the pane if modified is true
		if(modified){
			this.setTitle(m_Name + " - *" + m_Workspace.getWorkspaceName());
		}else{
			this.setTitle(m_Name + " - " + m_Workspace.getWorkspaceName());
		}
	}

	@Override
	public <T extends Component & Disposable> void addComponent(T component, GridBagConstraints gbc) {
		m_ContentPane.add(component, gbc);
		this.invalidate();
		this.validate();
	}
	
	@Override
	public void removeComponent(Component c){
		m_ContentPane.remove(c);
		this.invalidate();
		this.validate();
	}

	@Override
	public <T extends JMenu & Disposable> void addMenu(T menu) {
		m_MenuBar.add(menu);
	}
	
	@Override
	public void removeMenu(JMenu menu){
		m_MenuBar.remove(menu);
	}
	
	@Override
	public void addPreferencesPanel(PreferencesPanel panel, String tabName) {
		m_PreferencesWindow.addTab(panel, tabName);
	}
	
	@Override
	public void removePreferencesPanel(PreferencesPanel panel){
		m_PreferencesWindow.removeTab(panel);
	}
	
	@Override
	public <T extends Component & Disposable> void addHelpComponent(T component, String tabName) {
		m_HelpWindow.addTab(component, tabName);
	}
	
	@Override
	public void removeHelpComponent(Component comp){
		m_HelpWindow.removeTab(comp);
	}

	@Override
	public Future<?> newWorkspace() {
		return m_TaskManager.newWorkspace(m_Core);
	}
	

	@Override
	public Future<?> saveWorkspaceAs() throws SaveException {
		return m_TaskManager.saveWorkspace(m_Workspace);
	}

	@Override
	public Future<?> saveWorkspace() throws SaveException {
		return m_TaskManager.saveWorkspaceAs(m_Workspace);
	}

	@Override
	public Future<?> saveWorkspace(OutputStream out, String name) throws SaveException {
		return m_TaskManager.saveWorkspace(m_Workspace, out, name);
	}

	@Override
	public Future<?> openWorkspace() throws LoadException {
		return m_TaskManager.openWorkspace(m_Core, m_Workspace);
	}

	@Override
	public Future<?> openWorkspace(InputStream in, String name) throws LoadException {
		return m_TaskManager.openWorkspace(m_Core, m_Workspace, in, name);
	}

	@Override
	public Future<?> importWorkspace() throws ImportException {
		return m_TaskManager.importWorkspace(m_Workspace);
	}

	@Override
	public Future<?> importWorkspace(InputStream in, String name)
			throws ImportException {
		return m_TaskManager.importWorkspace(m_Workspace, in, name);
	}

	@Override
	public Future<?> importData(Class<? extends Data> dataClass) throws ReadException {
		return m_TaskManager.importData(m_Workspace, m_Core, dataClass);
	}

	@Override
	public Future<?> exportData(Data d) throws WriteException {
		String name = m_Workspace.getDataName(d);
		if(name == null) name = d.getClass().getSimpleName();
		return m_TaskManager.exportData(m_Core, d, name);
	}

	@Override
	public boolean close() {
		return m_CloseAction.close(this);
	}

	@Override
	public void pushNotification(Notification note) {
		m_NotePanel.pushNotification(note);
	}

	@Override
	public Frame getParentFrame() {
		return this;
	}

	@Override
	public Workspace getWorkspace() {
		return m_Workspace;
	}

	@Override
	public JProbeCore getCore() {
		return m_Core;
	}

	@Override
	public void update(Saveable s, SaveableEvent event) {
		if(s == m_Workspace){
			switch(event.type){
			case CHANGED:
				this.setModified(m_Workspace.unsavedChanges());
				break;
			default:
				Notification note = m_NoteProducer.toNotification(event);
				if(note != null){
					this.pushNotification(note);
				}
				break;
			}
		}
	}

	@Override
	public void update(Workspace source, WorkspaceEvent event) {
		if(source == m_Workspace){
			switch(event.type){
			case WORKSPACE_RENAMED:
				this.setModified(m_Workspace.unsavedChanges());
				break;
			default:
				//DO NOTHING
				break;
			}
		}
	}

	@Override
	public void update(Subject<Notification> observed, Notification notification) {
		if(observed == m_TaskManager){
			this.pushNotification(notification);
		}
	}

	@Override
	public void reportException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportWarning(String message) {
		// TODO Auto-generated method stub
		
	}

	
}
