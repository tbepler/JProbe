package plugins.jprobe.gui;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashSet;

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
import jprobe.services.JProbeCore;

public class JProbeGUIFrame extends JFrame implements JProbeGUI{
	private static final long serialVersionUID = 1L;
	
	public static final Dimension INITSIZE = new Dimension(800, 800);
	
	private JProbeCore core;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private Collection<GUIListener> listeners;
	
	JProbeGUIFrame(JProbeCore core, String name){
		super(name);
		this.core = core;
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
		menuBar.add(menu);
		menuBar.revalidate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.MENU_ADDED, responsible));
	}
	
	public void removeDropdownMenu(JMenu menu, Bundle responsible){
		menuBar.remove(menu);
		menuBar.revalidate();
		this.notifyListeners(new GUIEvent(this, GUIEvent.Type.MENU_REMOVED, responsible));
	}
	
}
