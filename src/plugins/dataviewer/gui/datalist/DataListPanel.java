package plugins.dataviewer.gui.datalist;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import plugins.dataviewer.gui.DataTabPane;
import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.JProbeCore;

public class DataListPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private DataListTable m_Table;
	
	public DataListPanel(JProbeCore core, JProbeGUI gui, DataTabPane tabPane){
		super(new BorderLayout());
		m_Table = new DataListTable(core, gui, tabPane);
		JScrollPane scroll = new JScrollPane(m_Table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		TitledBorder border = BorderFactory.createTitledBorder("Data");
		border.setTitleJustification(TitledBorder.CENTER);
		this.setBorder(border);
		this.add(scroll, BorderLayout.CENTER);
		//this.setMinimumSize(this.getPreferredSize());
	}
	
	public void cleanup(){
		m_Table.cleanup();
	}
	
	public GridBagConstraints getGridBagConstraints(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridy = 0;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.gridwidth = 1;
		gbc.weighty = 0.7;
		gbc.weightx = 0;
		return gbc;
	}
	
}
