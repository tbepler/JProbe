package old.view.data.display;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import old.core.Core;
import old.core.DataEvent;
import old.datatypes.DataType;
import old.view.data.DataContext;
import old.view.data.DataListener;

public class TabbedDataViewer extends JTabbedPane implements DataContext, Observer, ChangeListener{
	private static final long serialVersionUID = 1L;
	
	private List<DataListener> listeners;
	private Core core;
	
	public TabbedDataViewer(Core core){
		super();
		listeners = new ArrayList<DataListener>();
		this.core = core;
		this.core.addObserver(this);
		this.addChangeListener(this);
		this.addTab("test", new DataTab(null));
	}

	@Override
	public DataType getCurrentData() {
		if(this.getTabCount() <= 0) return null;
		return ((DataTab) this.getSelectedComponent()).data;
	}

	@Override
	public void addDataListener(DataListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeDataListener(DataListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void update(Observable o, Object event) {
		if(event instanceof DataEvent){
			DataEvent e = (DataEvent) event;
			if(e.event == DataEvent.EVENT_DATA_ADDED){
				this.addTab("data", new DataTab(e.data));
			}
		}

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		for(DataListener l : listeners){
			l.update(this);
		}
	}

}
