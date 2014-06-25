package plugins.jprobe.gui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import plugins.jprobe.gui.services.PreferencesPanel;
import util.gui.SwingUtils;

public class PreferencesWindow extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JPanel m_ContentPanel;
	private JTabbedPane m_TabPane;
	private JButton m_Apply;
	private JButton m_Cancel;
	private final Frame m_Owner;
	
	public PreferencesWindow(Frame owner, String title, boolean modal){
		super(owner, title, modal);
		m_Owner = owner;
		m_ContentPanel = new JPanel();
		m_ContentPanel.setLayout(new BoxLayout(m_ContentPanel, BoxLayout.Y_AXIS));
		this.setContentPane(m_ContentPanel);
		m_TabPane = new JTabbedPane();
		m_ContentPanel.add(m_TabPane);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		
		m_Apply = new JButton(Constants.PREF_APPLY_BUTTON_TEXT);
		m_Apply.addActionListener(this);
		buttonPanel.add(m_Apply);
		
		m_Cancel = new JButton(Constants.PREF_CANCEL_BUTTON_TEXT);
		m_Cancel.addActionListener(this);
		buttonPanel.add(m_Cancel);
		
		m_ContentPanel.add(buttonPanel);
		
		this.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentHidden(ComponentEvent e){
				closing();
			}
		});
		
		this.setPreferredSize(Constants.PREF_HELP_DEFAULT_DIM);
		this.pack();
		SwingUtils.centerWindow(this, owner);
	}
	
	@Override
	public void setVisible(boolean vis){
		if(vis)
			SwingUtils.centerWindow(this, m_Owner);
		super.setVisible(vis);
	}
	
	public void addTab(PreferencesPanel tab, String title){
		m_TabPane.addTab(title, tab);
		this.invalidate();
		this.validate();
	}
	
	public void removeTab(PreferencesPanel tab){
		m_TabPane.remove(tab);
		this.invalidate();
		this.validate();
	}
	
	protected void closing(){
		for(Component c : m_TabPane.getComponents()){
			if(c instanceof PreferencesPanel){
				PreferencesPanel panel = (PreferencesPanel) c;
				panel.close();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_Apply){
			for(Component c : m_TabPane.getComponents()){
				if(c instanceof PreferencesPanel){
					PreferencesPanel panel = (PreferencesPanel) c;
					panel.apply();
				}
			}
			this.setVisible(false);
		}
		if(e.getSource() == m_Cancel){
			this.setVisible(false);
		}
	}
	
}
