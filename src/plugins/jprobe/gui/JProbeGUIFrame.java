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
	
	public static final Dimension INITSIZE = new Dimension(800, 800);
	
	private Bundle bundle;
	private JProbeCore core;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private Queue<JMenu> pluginMenuItems;
	private TabDialogueWindow preferencesWindow;
	private TabDialogueWindow helpWindow;
	private DialogueMenu preferencesMenu;
	private DialogueMenu helpMenu;
	private Collection<GUIListener> listeners;
	
	JProbeGUIFrame(JProbeCore core, String name, Bundle bundle){
		super(name);
		this.bundle = bundle;
		this.core = core;
		pluginMenuItems = new PriorityQueue<JMenu>(10, new Comparator<JMenu>(){
			@Override
			public int compare(JMenu arg0, JMenu arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
		});
		listeners = new HashSet<GUIListener>();
		menuBar = new JMenuBar();
		contentPane = new JPanel(new GridBagLayout());
		contentPane.setOpaque(true);
		this.setJMenuBar(menuBar);
		this.setContentPane(contentPane);
		menuBar.setVisible(true);
		contentPane.setVisible(true);
		//override window closing event to make sure Felix is shutdown correctly
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent event){
				if(JOptionPane.showConfirmDialog(JProbeGUIFrame.this, "Exit JProbe?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION){
					JProbeGUIFrame.this.core.shutdown();
				}
			}
		});
		this.setPreferredSize(INITSIZE);
		this.pack();
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		this.setLocation(center.x-this.getWidth()/2, center.y-this.getHeight()/2);
		preferencesWindow = new TabDialogueWindow(this, "Preferences", true);
		helpWindow = new TabDialogueWindow(this, "Help", true);
		preferencesMenu = new DialogueMenu("Preferences", preferencesWindow);
		menuBar.add(preferencesMenu);
		helpMenu = new DialogueMenu("Help", helpWindow);
		menuBar.add(helpMenu);
	}
	
	@Override
	public void addGUIListener(GUIListener listener){
		listeners.add(listener);
	}
	
	@Override
	public void removeGUIListener(GUIListener listener){
		listeners.remove(listener);
	}
	
	private void notifyListeners(GUIEvent event){
		for(GUIListener l : listeners){
			l.update(event);
		}
	}
	
	private void checkDebugAndLog(String message){
		Debug debugLevel = Debug.getLevel();
		if(debugLevel == Debug.LOG || debugLevel == Debug.FULL){
			Log.getInstance().write(bundle, message);
		}
	}
	
	@Override
	public JProbeCore getJProbeCore(){
		return core;
	}
	
	public void addComponent(JComponent comp, GridBagConstraints c, Bundle responsible){
		contentPane.add(comp, c);
		comp.revalidate();
		comp.repaint();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.COMPONENT_ADDED, responsible));
	}
	
	public void removeComponent(JComponent comp, Bundle responsible){
		contentPane.remove(comp);
		contentPane.revalidate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.COMPONENT_REMOVED, responsible));
	}
	
	public void addDropdownMenu(JMenu menu, Bundle responsible){
		pluginMenuItems.add(menu);
		menuBar.removeAll();
		for(JMenu m : pluginMenuItems){
			menuBar.add(m);
		}
		menuBar.add(preferencesMenu);
		menuBar.add(helpMenu);
		menuBar.revalidate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.MENU_ADDED, responsible));
	}
	
	public void removeDropdownMenu(JMenu menu, Bundle responsible){
		pluginMenuItems.remove(menu);
		menuBar.remove(menu);
		menuBar.revalidate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.MENU_REMOVED, responsible));
	}

	@Override
	public Frame getGUIFrame() {
		return this;
	}

	@Override
	public void addHelpTab(JComponent component, Bundle responsible) {
		helpWindow.addTab(component, responsible.getSymbolicName());
		checkDebugAndLog("Help tab "+component.toString()+" added by plugin: "+responsible.getSymbolicName());
	}

	@Override
	public void removeHelpTab(JComponent component, Bundle responsible) {
		helpWindow.removeTab(component);
		checkDebugAndLog("Help tab "+component.toString()+" removed by plugin: "+responsible.getSymbolicName());
	}

	@Override
	public void addPreferencesTab(JComponent component, Bundle responsible) {
		preferencesWindow.addTab(component, responsible.getSymbolicName());
		checkDebugAndLog("Preferences tab "+component.toString()+" added by plugin: "+responsible.getSymbolicName());
	}

	@Override
	public void removePreferencesTab(JComponent component, Bundle responsible) {
		preferencesWindow.removeTab(component);
		checkDebugAndLog("Preferences tab "+component.toString()+" removed by plugin: "+responsible.getSymbolicName());
	}
	
}
