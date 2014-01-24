package plugins.dataviewer.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataListPanel extends JScrollPane implements CoreListener{
	private static final long serialVersionUID = 1L;
	
	private DataTabPane m_TabPane;
	private JPanel m_InnerPanel;
	private JProbeCore m_Core;
	private Map<Data, JLabel> m_NameLabels = new HashMap<Data, JLabel>();
	private Map<Data, JLabel> m_TypeLabels = new HashMap<Data, JLabel>();
	private int m_Row = 0;
	
	public DataListPanel(JProbeCore core, DataTabPane tabPane){
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		m_Core = core;
		m_Core.addCoreListener(this);
		m_TabPane = tabPane;
		m_InnerPanel = new VerticleScrollPanel(new GridBagLayout());
		for(int i=0; i<Constants.DATALIST_COL_HEADERS.length; i++){
			String header = Constants.DATALIST_COL_HEADERS[i];
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx=i;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			m_InnerPanel.add(new JLabel(header), gbc);
			m_InnerPanel.revalidate();
		}
		m_Row++;
		for(Data data : m_Core.getDataManager().getAllData()){
			this.add(data);
		}
		this.setViewportView(m_InnerPanel);
		//this.add(m_InnerPanel, BorderLayout.NORTH);
	}
	
	private void add(Data data){
		JLabel name = new JLabel(m_Core.getDataManager().getDataName(data));
		JLabel type = new JLabel(data.getClass().getSimpleName());
		m_NameLabels.put(data, name);
		m_TypeLabels.put(data, type);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy=m_Row;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		m_InnerPanel.add(name, gbc);
		gbc.gridx = 1;
		m_InnerPanel.add(type, gbc);
		m_Row++;
		m_InnerPanel.revalidate();
	}
	
	void cleanup(){
		m_Core.removeCoreListener(this);
	}
	
	public GridBagConstraints getGridBagConstraints(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 0;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.gridwidth = 1;
		gbc.weighty = 0.7;
		gbc.weightx = 0.1;
		return gbc;
	}

	@Override
	public void update(CoreEvent event) {
		if(event.type() == CoreEvent.Type.DATA_ADDED){
			Data added = event.getData();
			this.add(added);
		}
		if(event.type() == CoreEvent.Type.DATA_REMOVED){
			Data removed = event.getData();
			if(m_NameLabels.containsKey(removed)){
				m_InnerPanel.remove(m_NameLabels.get(removed));
				m_NameLabels.remove(removed);
			}
			if(m_TypeLabels.containsKey(removed)){
				m_InnerPanel.remove(m_TypeLabels.get(removed));
				m_TypeLabels.remove(removed);
			}
			m_InnerPanel.revalidate();
		}
		if(event.type() == CoreEvent.Type.DATA_NAME_CHANGE){
			Data changed = event.getData();
			if(m_NameLabels.containsKey(changed)){
				m_NameLabels.get(changed).setText(m_Core.getDataManager().getDataName(changed));
			}
			m_InnerPanel.revalidate();
		}
	}
	
}
