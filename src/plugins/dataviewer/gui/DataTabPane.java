package plugins.dataviewer.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTabbedPane;

import plugins.dataviewer.gui.services.DataViewer;
import plugins.dataviewer.gui.table.DataTab;

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.Data;
import jprobe.services.JProbeCore;

public class DataTabPane extends JTabbedPane implements CoreListener, DataViewer{

	private JProbeCore core;
	private Map<Data, DataTab> tabs;
	
	public DataTabPane(JProbeCore core){
		this.core = core;
		tabs = new HashMap<Data, DataTab>();
		core.addCoreListener(this);
	}

	@Override
	public void displayData(Data data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeData(Data data) {
		// TODO Auto-generated method stub
		
	}
	
	void cleanup(){
		core.removeCoreListener(this);
	}
	
	@Override
	public void update(CoreEvent event) {
		// TODO Auto-generated method stub
		
	}


	
	
}
