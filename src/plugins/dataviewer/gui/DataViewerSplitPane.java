package plugins.dataviewer.gui;

import java.awt.GridBagConstraints;

import javax.swing.JSplitPane;

import plugins.dataviewer.gui.datalist.DataListPanel;
import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.JProbeCore;

public class DataViewerSplitPane extends JSplitPane{
	private static final long serialVersionUID = 1L;
	
	private final DataTabPane m_DataTab;
	private final DataListPanel m_DataList;
	
	public DataViewerSplitPane(JProbeCore core, JProbeGUI gui){
		super(JSplitPane.HORIZONTAL_SPLIT);
		m_DataTab = new DataTabPane(core.getDataManager());
		m_DataList = new DataListPanel(core, gui, m_DataTab);
		this.setOneTouchExpandable(true);
		this.setContinuousLayout(true);
		this.setLeftComponent(m_DataTab);
		this.setRightComponent(m_DataList);
		this.setResizeWeight(1.0);
	}
	
	public void cleanup(){
		m_DataTab.cleanup();
		m_DataList.cleanup();
	}
	
	public GridBagConstraints getGridBagConstraints(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		return gbc;
	}

}
