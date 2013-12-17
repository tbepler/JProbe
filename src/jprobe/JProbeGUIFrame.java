package jprobe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JProbeGUIFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JProbe core;
	private JPanel contentPane;
	private JMenuBar menuBar;
	
	JProbeGUIFrame(JProbe core, String name){
		super(name);
		this.core = core;
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
	
	void addComponent(JComponent comp, GridBagConstraints c){
		contentPane.add(comp, c);
		comp.revalidate();
		comp.repaint();
	}
	
	void removeComponent(JComponent comp){
		contentPane.remove(comp);
		contentPane.revalidate();
	}
	
	void addDropdownMenu(JMenu menu){
		menuBar.add(menu);
		menuBar.revalidate();
	}
	
	void removeDropdownMenu(JMenu menu){
		menuBar.remove(menu);
		menuBar.revalidate();
	}
	
}
