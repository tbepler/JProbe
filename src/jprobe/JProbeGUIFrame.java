package jprobe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;

public class JProbeGUIFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JProbe core;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private Collection<CoreListener> listeners;
	
	JProbeGUIFrame(JProbe core, String name){
		super(name);
		this.core = core;
		listeners = new HashSet<CoreListener>();
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
					System.exit(0);
				}
			}
		});
		this.setLocationRelativeTo(null);
	}
	
	public void addListener(CoreListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(CoreListener listener){
		listeners.remove(listener);
	}
	
	private void notifyListeners(CoreEvent event){
		for(CoreListener l : listeners){
			l.update(event);
		}
	}
	
	void addComponent(JComponent comp, GridBagConstraints c, Bundle responsible){
		contentPane.add(comp, c);
		comp.revalidate();
		comp.repaint();
		this.notifyListeners(new CoreEvent(core, CoreEvent.Type.GUI_COMPONENT_ADDED, responsible));
	}
	
	void removeComponent(JComponent comp, Bundle responsible){
		contentPane.remove(comp);
		contentPane.revalidate();
		this.notifyListeners(new CoreEvent(core, CoreEvent.Type.GUI_COMPONENT_REMOVED, responsible));
	}
	
	void addDropdownMenu(JMenu menu, Bundle responsible){
		menuBar.add(menu);
		menuBar.revalidate();
		this.notifyListeners(new CoreEvent(core, CoreEvent.Type.GUI_MENU_ADDED, responsible));
	}
	
	void removeDropdownMenu(JMenu menu, Bundle responsible){
		menuBar.remove(menu);
		menuBar.revalidate();
		this.notifyListeners(new CoreEvent(core, CoreEvent.Type.GUI_MENU_REMOVED, responsible));
	}
	
}
