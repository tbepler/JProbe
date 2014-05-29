package plugins.jprobe.gui.utils;

import java.awt.Frame;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import plugins.jprobe.gui.Constants;

public class TabDialogueWindow extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabPane;
	
	public TabDialogueWindow(Frame owner, String title, boolean modal){
		super(owner, title, modal);
		tabPane = new JTabbedPane();
		this.setContentPane(tabPane);
		this.setPreferredSize(Constants.PREF_HELP_DEFAULT_DIM);
		this.pack();
		this.setLocation(owner.getX()+owner.getWidth()/2-this.getWidth()/2, owner.getY()+owner.getHeight()/2-this.getHeight()/2);
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
