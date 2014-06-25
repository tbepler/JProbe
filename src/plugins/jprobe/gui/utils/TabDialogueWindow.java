package plugins.jprobe.gui.utils;

import java.awt.Frame;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import plugins.jprobe.gui.Constants;
import util.gui.SwingUtils;

public class TabDialogueWindow extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane m_TabPane;
	private final Frame m_Owner;
	
	public TabDialogueWindow(Frame owner, String title, boolean modal){
		super(owner, title, modal);
		m_Owner = owner;
		m_TabPane = new JTabbedPane();
		this.setContentPane(m_TabPane);
		this.setPreferredSize(Constants.PREF_HELP_DEFAULT_DIM);
		this.pack();
	}
	
	@Override
	public void setVisible(boolean vis){
		if(vis)
			SwingUtils.centerWindow(this, m_Owner);
		super.setVisible(vis);
	}
	
	public void addTab(JComponent tab, String title){
		m_TabPane.addTab(title, tab);
		this.invalidate();
		this.validate();
	}
	
	public void removeTab(JComponent tab){
		m_TabPane.remove(tab);
		this.invalidate();
		this.validate();
	}
	
	
}
