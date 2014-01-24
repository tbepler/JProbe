package plugins.functions.gui.dialog.data;

import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import plugins.functions.gui.Constants;
import plugins.functions.gui.utils.StateListener;
import plugins.functions.gui.utils.ValidStateNotifier;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.function.DataParameter;

public class DataComboBox extends JComboBox<String> implements CoreListener, ValidStateNotifier{
	private static final long serialVersionUID = 1L;
	
	private static final String SELECT_NOTHING = "-"; 
	
	private ItemListener m_ItemChangeListener = new ItemListener(){
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED){
				m_Valid = m_DataParam.isValid(m_Displayed.get(getSelectedItem()));
				notifyListeners();
			}
			if(e.getStateChange() == ItemEvent.DESELECTED){
				m_Valid = false;
				notifyListeners();
			}
		}
	};
	private DataParameter m_DataParam;
	private JProbeCore m_Core;
	private Map<String, Data> m_Displayed;
	private Map<Data, String> m_Data;
	private boolean m_Valid;
	private Collection<StateListener> m_Listeners = new HashSet<StateListener>();
	
	public DataComboBox(DataParameter dataParam, JProbeCore core){
		super();
		this.addItemListener(m_ItemChangeListener);
		this.setPrototypeDisplayValue(Constants.DATACOMBOBOX_PROTOTYPE_DISPLAY);
		m_Valid = dataParam.isOptional();
		m_DataParam = dataParam;
		m_Core = core;
		m_Core.addCoreListener(this);
		m_Displayed = new HashMap<String, Data>();
		m_Data = new HashMap<Data, String>();
		if(m_DataParam.isOptional()){
			this.addItem(SELECT_NOTHING);
			m_Displayed.put(SELECT_NOTHING, null);
		}
		for(Data d : m_Core.getDataManager().getAllData()){
			if(isValid(d)){
				String name = m_Core.getDataManager().getDataName(d);
				m_Displayed.put(name, d);
				m_Data.put(d, name);
				this.addItem(name);
			}
		}
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
		m_Data.put(d, name);
		this.addItem(name);
		this.resizeWindow();
		//this.revalidate();
	}
	
	private void removeData(Data d){
		String name = m_Data.get(d);
		if(m_Displayed.containsKey(name)){
			m_Displayed.remove(name);
			m_Data.remove(d);
			this.removeItem(name);
			this.resizeWindow();
			//this.revalidate();
		}
	}
	
	private void resizeWindow(){
		Window ancestor = SwingUtilities.getWindowAncestor(this);
		if(ancestor != null){
			ancestor.pack();
		}
	}
	
	private void rename(String oldName, String newName){
		if(m_Displayed.containsKey(oldName)){
			Data changed = m_Displayed.get(oldName);
			m_Data.put(changed, newName);
			m_Displayed.remove(oldName);
			if(this.getSelectedItem().equals(oldName)){
				int index = this.getSelectedIndex();
				this.removeItem(oldName);
				m_Displayed.put(newName, changed);
				this.insertItemAt(newName, index);
				this.setSelectedIndex(index);
			}else{
				int index = this.getIndex(oldName);
				if(index < 0) return;
				this.removeItem(oldName);
				m_Displayed.put(newName, changed);
				this.insertItemAt(newName, index);
			}
		}
	}
	
	private int getIndex(String item){
		for(int i=0; i<this.getItemCount(); i++){
			if(this.getItemAt(i).equals(item)) return i;
		}
		return -1;
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
	
	public Data getSelectedData(){
		return m_Displayed.get(this.getSelectedItem());
	}
	
	@Override
	public boolean isStateValid(){
		return m_Valid;
	}
	
	protected void notifyListeners(){
		for(StateListener l : m_Listeners){
			l.update(this);
		}
	}

	@Override
	public void addStateListener(StateListener l) {
		m_Listeners.add(l);
	}

	@Override
	public void removeStateListener(StateListener l) {
		m_Listeners.remove(l);
	}
	
}
