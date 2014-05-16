package jprobe.services.functionRework.components;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataComboBox extends JComboBox<String>{
	private static final long serialVersionUID = 1L;

	public static final String DATACOMBOBOX_PROTOTYPE_DISPLAY = "SomeDataHere";
	
	private final JProbeCore m_Core;
	private final Map<String, Data> m_Displayed = new HashMap<String, Data>();
	private final Map<Data, String> m_Data = new HashMap<Data, String>();
	
	public DataComboBox(JProbeCore core){
		super();
		this.setPrototypeDisplayValue(DATACOMBOBOX_PROTOTYPE_DISPLAY);
		m_Core = core;
	}
	
	public void addData(Data d){
		String name = m_Core.getDataManager().getDataName(d);
		m_Displayed.put(name, d);
		m_Data.put(d, name);
		this.addItem(name);
		this.resizeWindow();
		//this.revalidate();
	}
	
	public void removeData(Data d){
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
	
	public void rename(String oldName, String newName){
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
	
	public Data getSelectedData(){
		return m_Displayed.get(this.getSelectedItem());
	}
	
}
