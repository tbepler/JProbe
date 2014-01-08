package plugins.jprobe.gui;

import java.awt.Frame;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

public class TabDialogueWindow extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabPane;
	
	public TabDialogueWindow(Frame owner, String title, boolean modal){
		super(owner, title, modal);
		tabPane = new JTabbedPane();
		this.setContentPane(tabPane);
		this.setLocationRelativeTo(owner);
		this.pack();
	}
	
	public void addTab(JComponent tab, String title){
		tabPane.addTab(title, tab);
		this.revalidate();
	}
	
	public void removeTab(JComponent tab){
		tabPane.remove(tab);
		this.revalidate();
	}
	
	
}
