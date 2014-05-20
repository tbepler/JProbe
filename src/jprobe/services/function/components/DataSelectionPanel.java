package jprobe.services.function.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JPanel;

import util.observer.Observer;
import util.observer.Subject;
import jprobe.Constants;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataSelectionPanel<D extends Data> extends JPanel implements ItemListener, ActionListener, Subject<D>{
	private static final long serialVersionUID = 1L;
	
	public static interface OnClose{
		public void close();
	}
	
	private final Collection<Observer<D>> m_Obs = new HashSet<Observer<D>>();
	
	private final DataComboBox<D> m_DataBox;
	private final JButton m_CloseButton;
	private OnClose m_CloseAction = new OnClose(){

		@Override
		public void close() {
			//do nothing
		}
		
	};
	
	public DataSelectionPanel(JProbeCore core, boolean optional){
		super();
		m_DataBox = new DataComboBox<D>(core);
		m_DataBox.addItemListener(this);
		this.add(m_DataBox);
		m_CloseButton = new IconButton(Constants.X_ICON, Constants.X_HIGHLIGHTED_ICON, Constants.X_CLICKED_ICON);
		m_CloseButton.addActionListener(this);
		m_CloseButton.setEnabled(optional);
		this.add(m_CloseButton);
		if(optional){
			m_DataBox.addData(null);
		}
	}
	
	public void setCloseAction(OnClose closeAction){
		m_CloseAction = closeAction;
	}
	
	public void addData(D d){
		m_DataBox.addData(d);
	}
	
	public void removeData(D d){
		m_DataBox.removeData(d);
	}
	
	public void renameData(String oldName, String newName){
		m_DataBox.rename(oldName, newName);
	}
	
	public boolean isOptional(){
		return m_CloseButton.isEnabled();
	}
	
	public void setOptional(boolean optional){
		if(this.isOptional() != optional){
			if(optional){
				m_DataBox.addData(null);
			}else{
				m_DataBox.removeData(null);
			}
			m_CloseButton.setEnabled(optional);
		}
	}
	
	public Data getSelectedData(){
		return m_DataBox.getSelectedData();
	}
	
	protected void close(){
		m_CloseAction.close();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == m_DataBox){
			D selected = m_DataBox.getSelectedData();
			this.notifyObservers(selected);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_CloseButton){
			this.close();
		}
	}
	
	protected void notifyObservers(D d){
		for(Observer<D> obs : m_Obs){
			obs.update(this, d);
		}
	}

	@Override
	public void register(Observer<D> obs) {
		m_Obs.add(obs);
	}

	@Override
	public void unregister(Observer<D> obs) {
		m_Obs.remove(obs);
	}
	
}
