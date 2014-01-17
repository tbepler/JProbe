package plugins.functions.gui.dialog;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.function.DataParameter;

public class DataComboBox extends JPanel implements ItemListener, CoreListener{
	private static final long serialVersionUID = 1L;
	
	private static final String SELECT_NOTHING = "-"; 
	
	private DataParameter m_DataParam;
	private JProbeCore m_Core;
	private Map<String, Data> m_Displayed;
	private JComboBox<String> m_ComboBox;
	private Collection<ActionListener> m_Listeners = new HashSet<ActionListener>();
	
	public DataComboBox(DataParameter dataParam, JProbeCore core){
		super();
		m_DataParam = dataParam;
		m_Core = core;
		m_Core.addCoreListener(this);
		m_Displayed = new HashMap<String, Data>();
		m_ComboBox = new JComboBox<String>();
		if(m_DataParam.isOptional()){
			m_ComboBox.addItem(SELECT_NOTHING);
		}
		for(Data d : m_Core.getDataManager().getAllData()){
			if(isValid(d)){
				String name = m_Core.getDataManager().getDataName(d);
				m_Displayed.put(name, d);
				m_ComboBox.addItem(name);
			}
		}
	}
	
	public void addActionListener(ActionListener l){
		m_Listeners.add(l);
	}
	
	public void removeActionListener(ActionListener l){
		m_Listeners.remove(l);
	}
	
	public void cleanup(){
		m_Core.removeCoreListener(this);
		m_Listeners.clear();
	}
	
	private boolean isValid(Data d){
		return d.getClass() == m_DataParam.getType() && m_DataParam.isValid(d);
	}
	
	private void addData(Data d){
		String name = m_Core.getDataManager().getDataName(d);
		m_Displayed.put(name, d);
		m_ComboBox.addItem(name);
		m_ComboBox.revalidate();
	}
	
	private void removeData(Data d){
		String name = m_Core.getDataManager().getDataName(d);
		if(m_Displayed.containsKey(name)){
			m_Displayed.remove(name);
			m_ComboBox.removeItem(name);
			m_ComboBox.revalidate();
		}
	}
	
	private void rename(String oldName, String newName){
		if(m_Displayed.containsKey(oldName)){
			Data changed = m_Displayed.get(oldName);
			m_Displayed.remove(oldName);
			m_ComboBox.removeItem(oldName);
			m_Displayed.put(newName, changed);
			m_ComboBox.addItem(newName);
		}
	}

	@Override
	public void update(CoreEvent event) {
		if(event.type() == CoreEvent.Type.DATA_ADDED){
			Data added = event.getData();
			if(added.getClass() == m_DataParam.getType() && m_DataParam.isValid(added)){
				this.addData(added);
			}
		}
		if(event.type() == CoreEvent.Type.DATA_REMOVED){
			Data removed = event.getData();
			this.removeData(removed);
		}
		if(event.type() == CoreEvent.Type.DATA_NAME_CHANGE){
			String oldName = event.getOldName();
			String newName = event.getNewName();
			this.rename(oldName, newName);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
