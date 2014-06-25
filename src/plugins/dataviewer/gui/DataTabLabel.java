package plugins.dataviewer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DataTabLabel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private DataTabPane m_TabbedPane;
	private DataTab m_Tab;
	private JLabel m_Label;	
	
	public DataTabLabel(DataTabPane tabbedPane, DataTab tab, String title){
		super();
		this.setOpaque(false);
		m_TabbedPane = tabbedPane;
		m_Tab = tab;
		m_Label = new JLabel(title);
		m_Label.setOpaque(false);
		this.add(m_Label);
		JButton close = new IconButton(Constants.getXIcon(), Constants.getXHighlightedIcon(), Constants.getXClickedIcon());
		close.addActionListener(this);
		this.add(close);
	}
	
	public void setTitle(String title){
		m_Label.setText(title);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		m_TabbedPane.closeTab(m_Tab);
	}
	
}
