package plugins.functions.gui.dialog;

import java.util.Collection;
import java.util.HashSet;

import javax.swing.JPanel;

import plugins.functions.gui.utils.StateListener;
import plugins.functions.gui.utils.StateNotifier;
import plugins.functions.gui.utils.ValidStateNotifier;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;

public class ParameterPanel extends JPanel implements ValidStateNotifier, StateListener{
	private static final long serialVersionUID = 1L;
	
	private FieldParameter[] m_FieldParams;
	private DataPanel m_DataPanel;
	private boolean m_Valid;
	private Collection<StateListener> m_Listeners = new HashSet<StateListener>();
	
	public ParameterPanel(DataParameter[] dataParams, FieldParameter[] fieldParams, JProbeCore core){
		super();
		m_DataPanel = new DataPanel(dataParams, core);
		m_DataPanel.addStateListener(this);
		this.add(m_DataPanel);
		m_Valid = this.componentsValid();
		m_FieldParams = fieldParams;
	}
	
	public Data[] getDataArgs(){
		return m_DataPanel.getSelectedData();
	}
	
	public Field[] getFieldArgs(){
		return null;
	}
	
	private boolean componentsValid(){
		return m_DataPanel.isStateValid();
	}

	@Override
	public void addStateListener(StateListener l) {
		m_Listeners.add(l);
	}

	@Override
	public void removeStateListener(StateListener l) {
		m_Listeners.remove(l);
	}
	
	private void notifyListeners(){
		for(StateListener l : m_Listeners){
			l.update(this);
		}
	}

	@Override
	public boolean isStateValid() {
		return m_Valid;
	}

	@Override
	public void update(StateNotifier source) {
		boolean newState = this.componentsValid();
		if(m_Valid != newState){
			m_Valid = newState;
			this.notifyListeners();
		}
	}
	
}
