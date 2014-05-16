package jprobe.services.functionRework.components;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataComboBox extends JComboBox<String> implements CoreListener{
	private static final long serialVersionUID = 1L;
	
	public static interface DataValidFunction{
		public boolean isValid(Data d);
	}

	public static final String DATACOMBOBOX_PROTOTYPE_DISPLAY = "SomeDataHere";
	
	private final JProbeCore m_Core;
	private final DataValidFunction m_ValidFunction;
	private final Map<String, Data> m_Displayed = new HashMap<String, Data>();
	private final Map<Data, String> m_Data = new HashMap<Data, String>();
	
	public DataComboBox(DataValidFunction function, JProbeCore core){
		super();
		this.setPrototypeDisplayValue(DATACOMBOBOX_PROTOTYPE_DISPLAY);
		m_Core = core;
		m_Core.addCoreListener(this);
		m_ValidFunction = function;
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
	}
	
	private boolean isValid(Data d){
		return m_ValidFunction.isValid(d);
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
			if(this.isValid(added)){
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
	
}
