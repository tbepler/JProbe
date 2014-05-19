package jprobe.services.functionRework.components;

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

public class DataSelectionPanel extends JPanel implements ItemListener, ActionListener, Subject<Data>{
	private static final long serialVersionUID = 1L;
	
	public static interface OnClose{
		public void close();
	}
	
	private final Collection<Observer<Data>> m_Obs = new HashSet<Observer<Data>>();
	
	private final DataComboBox m_DataBox;
	private final JButton m_CloseButton;
	private final OnClose m_CloseAction;
	
	public DataSelectionPanel(JProbeCore core, OnClose closeAction, boolean optional){
		super();
		m_CloseAction = closeAction;
		m_DataBox = new DataComboBox(core);
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
	
	public void addData(Data d){
		m_DataBox.addData(d);
	}
	
	public void removeData(Data d){
		m_DataBox.removeData(d);
	}
	
	public boolean isOptional(){
		return m_CloseButton.isEnabled();
	}
	
	public void setOptional(boolean optional){
		
		m_CloseButton.setEnabled(optional);
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
			Data selected = m_DataBox.getSelectedData();
			this.notifyObservers(selected);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_CloseButton){
			this.close();
		}
	}
	
	protected void notifyObservers(Data d){
		for(Observer<Data> obs : m_Obs){
			obs.update(this, d);
		}
	}

	@Override
	public void register(Observer<Data> obs) {
		m_Obs.add(obs);
	}

	@Override
	public void unregister(Observer<Data> obs) {
		m_Obs.remove(obs);
	}
	
}
