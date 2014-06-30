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
import plugins.jprobe.gui.services.JProbeWindow;
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

public class JProbeFrame extends JFrame implements JProbeWindow{
	private static final long serialVersionUID = 1L;
	
	private final JFileChooser m_ImportChooser = new JFileChooser();
	private final JFileChooser m_ExportChooser = new JFileChooser();
	

	private final PreferencesWindow m_PreferencesWindow = new PreferencesWindow(this, Constants.PREFERENCES_NAME, true);
	private final TabDialogueWindow m_HelpWindow = new TabDialogueWindow(this, Constants.HELP_NAME, true);
	
	private final JProbeCore m_Core;
	private final JPanel m_ContentPane;
	private final JMenuBar m_MenuBar;
	
	
	private NotificationPanel m_NotePanel;
	
	public JProbeFrame(JProbeCore core, Workspace w, GUIConfig config){
		super();
		m_Core = core;
		
		m_ContentPane = this.createContentPane();
		this.setContentPane(m_ContentPane);
		
		m_MenuBar = this.createMenuBar(m_PreferencesWindow, m_HelpWindow);
		this.setJMenuBar(m_MenuBar);
		
		this.initCloseOperation();
		
		m_NotePanel = initNotificationPanel(m_Core);
		
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
	
	private void checkDebugAndLog(String message){
		Debug debugLevel = Debug.getLevel();
		if(debugLevel == Debug.LOG || debugLevel == Debug.FULL){
			JProbeLog.getInstance().write(m_Bundle, message);
		}
	}
	
	public JFileChooser getExportChooser(){
		return m_ExportChooser;
	}
	
	public JFileChooser getImportChooser(){
		return m_ImportChooser;
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
	
}
