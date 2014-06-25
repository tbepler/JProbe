package plugins.dataviewer.gui;

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
	
	//public static final Dimension PREFERRED = new Dimension(800, 800);
	
	private DataManager m_DataManager;
	private Map<Data, DataTab> m_Tabs;
	private Map<Data, DataTabLabel> m_TabLables;
	private GridBagConstraints m_Constraints;
	
	public DataTabPane(DataManager dataManager){
		super();
		m_DataManager = dataManager;
		m_DataManager.addListener(this);
		m_Constraints = new GridBagConstraints();
		m_Constraints.fill = GridBagConstraints.BOTH;
		m_Constraints.weightx = 0.7;
		m_Constraints.weighty = 0.7;
		m_Constraints.gridheight = GridBagConstraints.REMAINDER;
		m_Constraints.gridwidth = 3;
		m_Tabs = new HashMap<Data, DataTab>();
		m_TabLables = new HashMap<Data, DataTabLabel>();
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				initTabs();
			}
			
		});
	}
	
	public void initTabs(){
		for(Data d : m_DataManager.getAllData()){
			DataTab tab = new  DataTab(d);
			m_Tabs.put(d, tab);
			this.addTab("", tab);
			int index = this.indexOfComponent(tab);
			DataTabLabel lable = new DataTabLabel(this, tab, m_DataManager.getDataName(d));
			m_TabLables.put(d, lable);
			this.setTabComponentAt(index, lable);
		}
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
		}
	}

	@Override
	public void closeData(Data data) {
		this.remove(m_Tabs.get(data));
		m_Tabs.remove(data);
		m_TabLables.remove(data);
	}
	
	public void closeTab(DataTab tab){
		this.remove(tab);
		m_Tabs.remove(tab.getData());
		m_TabLables.remove(tab.getData());
	}
	
	public void clear(){
		this.removeAll();
		m_Tabs.clear();
		m_TabLables.clear();
	}
	
	void cleanup(){
		m_DataManager.removeListener(this);
	}
	
	@Override
	public void update(final CoreEvent event) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				process(event);
			}
			
		});
		
	}
	
	private void process(CoreEvent event){
		switch(event.type()){
		case DATA_ADDED:
			displayData(event.getData());
			break;
		case DATA_NAME_CHANGE:
			m_TabLables.get(event.getData()).setTitle(m_DataManager.getDataName(event.getData()));
			break;
		case DATA_REMOVED:
			closeData(event.getData());
			break;
		case WORKSPACE_CLEARED:
			this.clear();
			break;
		case WORKSPACE_LOADED:
			this.initTabs();
			break;
		default:
			break;
		}	
	}


	
	
}
