package jprobe.services.function.components;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

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
	
	private final Map<String, D> m_Displayed = new HashMap<String, D>();
	private final Map<D, String> m_Data = new HashMap<D, String>();
	
	public DataComboBox(){
		super(new SortedModel());
		this.setPrototypeDisplayValue(DATACOMBOBOX_PROTOTYPE_DISPLAY);
	}
	
	public void addData(D d, String name){
		if(m_Data.containsKey(d)){
			this.rename(d, name);
			return;
		}
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
	
	public void rename(D data, String newName){
		if(m_Data.containsKey(data)){
			String oldName = m_Data.get(data);
			m_Displayed.remove(oldName);
			m_Displayed.put(newName, data);
			m_Data.put(data, newName);
			boolean selected = this.getSelectedItem().equals(oldName);
			this.removeItem(oldName);
			this.addItem(newName);
			if(selected){
				this.setSelectedItem(newName);
			}
		}
	}
	
	public D getSelectedData(){
		return m_Displayed.get(this.getSelectedItem());
	}
	
}
