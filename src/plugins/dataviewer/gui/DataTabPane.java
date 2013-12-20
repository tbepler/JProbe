package plugins.dataviewer.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTabbedPane;

import plugins.dataviewer.gui.services.DataViewer;
import plugins.dataviewer.gui.table.DataTab;
import plugins.dataviewer.gui.table.DataTable;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.Data;
import jprobe.services.JProbeCore;

public class DataTabPane extends JTabbedPane implements CoreListener, DataViewer{
	private static final long serialVersionUID = 1L;
	
	public static final Dimension PREFERRED = new Dimension(800, 800);
	
	private JProbeCore core;
	private Map<Data, DataTab> tabs;
	private GridBagConstraints constraints;
	
	public DataTabPane(JProbeCore core){
		super();
		this.setPreferredSize(PREFERRED);
		this.core = core;
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0.7;
		constraints.weighty = 0.7;
		constraints.gridheight = GridBagConstraints.REMAINDER;
		constraints.gridwidth = 3;
		tabs = new HashMap<Data, DataTab>();
		core.addCoreListener(this);
		for(Data d : core.getData()){
			DataTab tab = new  DataTab(d);
			tabs.put(d, tab);
			this.addTab(core.getName(d), tab);
		}
		this.revalidate();
	}
	
	public GridBagConstraints getGridBagConstraints(){
		return constraints;
	}

	@Override
	public void displayData(Data data) {
		DataTab tab = new DataTab(data);
		tabs.put(data, tab);
		this.addTab(core.getName(data), tab);
		this.revalidate();
	}

	@Override
	public void closeData(Data data) {
		this.remove(tabs.get(data));
		tabs.remove(data);
		this.revalidate();
	}
	
	void cleanup(){
		core.removeCoreListener(this);
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
			this.setTitleAt(this.indexOfComponent(tabs.get(event.getData())), core.getName(event.getData()));
		}
	}


	
	
}
