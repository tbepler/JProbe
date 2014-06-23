package jprobe.services.function.components;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataComboBox<D extends Data> extends JComboBox<String>{
	private static final long serialVersionUID = 1L;
	
	private static class SortedModel extends DefaultComboBoxModel<String>{
		private static final long serialVersionUID = 1L;
		@Override
		public void addElement(String s){
			this.insertElementAt(s, 0);
		}
		@Override
		public void insertElementAt(String s, int index){
			for(index=0; index<this.getSize(); index++){
				if(this.getElementAt(index).compareTo(s) > 0)
					break;
			}
			super.insertElementAt(s, index);
		}
	}

	public static final String DATACOMBOBOX_PROTOTYPE_DISPLAY = "SomeDataHere";
	
	private final JProbeCore m_Core;
	private final Map<String, D> m_Displayed = new HashMap<String, D>();
	private final Map<D, String> m_Data = new HashMap<D, String>();
	
	public DataComboBox(JProbeCore core){
		super(new SortedModel());
		this.setPrototypeDisplayValue(DATACOMBOBOX_PROTOTYPE_DISPLAY);
		m_Core = core;
	}
	
	public JProbeCore getCore(){
		return m_Core;
	}
	
	public void addData(D d){
		if(m_Data.containsKey(d)){
			return;
		}
		String name;
		if(d == null){
			name = "";
		}else{
			name = m_Core.getDataManager().getDataName(d);
		}
		if(name == null) return;
		m_Displayed.put(name, d);
		m_Data.put(d, name);
		this.addItem(name);
		if(this.getSelectedIndex() == -1){
			this.setSelectedItem(name);
		}
		this.resizeWindow();
		//this.revalidate();
	}
	
	public void removeData(D d){
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
			D changed = m_Displayed.get(oldName);
			m_Data.put(changed, newName);
			m_Displayed.remove(oldName);
			if(this.getSelectedItem().equals(oldName)){
				this.removeItem(oldName);
				m_Displayed.put(newName, changed);
				this.addItem(newName);
				this.setSelectedItem(newName);
			}else{
				this.removeItem(oldName);
				m_Displayed.put(newName, changed);
				this.addItem(newName);
			}
		}
	}
	
	public D getSelectedData(){
		return m_Displayed.get(this.getSelectedItem());
	}
	
}
