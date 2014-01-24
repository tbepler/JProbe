package plugins.dataviewer.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import plugins.dataviewer.gui.services.DataViewer;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.DataManager;
import jprobe.services.data.Data;

public class DataTabPane extends JTabbedPane implements CoreListener, DataViewer{
	private static final long serialVersionUID = 1L;
	
	public static final Dimension PREFERRED = new Dimension(800, 800);
	
	private DataManager m_DataManager;
	private Map<Data, DataTab> m_Tabs;
	private Map<Data, DataTabLabel> m_TabLables;
	private GridBagConstraints m_Constraints;
	
	public DataTabPane(DataManager dataManager){
		super();
		this.setPreferredSize(PREFERRED);
		this.m_DataManager = dataManager;
		m_Constraints = new GridBagConstraints();
		m_Constraints.fill = GridBagConstraints.BOTH;
		m_Constraints.weightx = 0.7;
		m_Constraints.weighty = 0.7;
		m_Constraints.gridheight = GridBagConstraints.REMAINDER;
		m_Constraints.gridwidth = 3;
		m_Tabs = new HashMap<Data, DataTab>();
		m_TabLables = new HashMap<Data, DataTabLabel>();
		dataManager.addListener(this);
		for(Data d : dataManager.getAllData()){
			DataTab tab = new  DataTab(d);
			m_Tabs.put(d, tab);
			this.addTab("", tab);
			int index = this.indexOfComponent(tab);
			DataTabLabel lable = new DataTabLabel(this, tab, m_DataManager.getDataName(d));
			m_TabLables.put(d, lable);
			this.setTabComponentAt(index, lable);
		}
		this.revalidate();
	}
	
	public GridBagConstraints getGridBagConstraints(){
		return m_Constraints;
	}
	
	@Override
	public void selectData(Data data){
		if(!m_Tabs.containsKey(data)){
			this.displayData(data);
		}
		final DataTab select = m_Tabs.get(data);
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				try{
					DataTabPane.this.setSelectedComponent(select);
				} catch (Exception e){
					//do nothing
				}
			}
		});
	}
	
	@Override
	public void displayData(Data data) {
		if(!m_Tabs.containsKey(data)){
			DataTab tab = new DataTab(data);
			m_Tabs.put(data, tab);
			this.addTab("", tab);
			int index = this.indexOfComponent(tab);
			DataTabLabel lable = new DataTabLabel(this, tab, m_DataManager.getDataName(data));
			m_TabLables.put(data, lable);
			this.setTabComponentAt(index, lable);
			this.revalidate();
		}
	}

	@Override
	public void closeData(Data data) {
		this.remove(m_Tabs.get(data));
		m_Tabs.remove(data);
		m_TabLables.remove(data);
		this.revalidate();
	}
	
	public void closeTab(DataTab tab){
		this.remove(tab);
		m_Tabs.remove(tab.getData());
		m_TabLables.remove(tab.getData());
		this.revalidate();
	}
	
	void cleanup(){
		m_DataManager.removeListener(this);
	}
	
	@Override
	public void update(CoreEvent event) {
		if(event.type() == CoreEvent.Type.DATA_ADDED){
			displayData(event.getData());
		}
		if(event.type() == CoreEvent.Type.DATA_REMOVED){
			closeData(event.getData());
		}
		if(event.type() == CoreEvent.Type.DATA_NAME_CHANGE){
			m_TabLables.get(event.getData()).setTitle(m_DataManager.getDataName(event.getData()));
		}
	}


	
	
}
