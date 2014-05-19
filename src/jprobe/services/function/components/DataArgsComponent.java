package jprobe.services.function.components;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import util.observer.Observer;
import util.observer.Subject;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataArgsComponent extends JPanel implements ValidNotifier, Observer<Data>, CoreListener{
	private static final long serialVersionUID = 1L;
	
	public static interface DataValidFunction{
		public boolean isValid(Data d);
	}
	
	private final Collection<ValidListener> m_Listeners = new HashSet<ValidListener>();
	
	private final List<DataSelectionPanel> m_DataComps = new ArrayList<DataSelectionPanel>();
	private final List<Data> m_SelectedData = new ArrayList<Data>();
	
	private final JProbeCore m_Core;
	private final int m_MinArgs;
	private final int m_MaxArgs;
	private final boolean m_AllowDuplicates;
	private final DataValidFunction m_ValidFunction;
	
	private boolean m_Valid;
	
	public DataArgsComponent(JProbeCore core, int minArgs, int maxArgs, boolean allowDuplicates, DataValidFunction validFunction){
		super();
		m_Core = core;
		m_Core.addCoreListener(this);
		m_MinArgs = minArgs;
		m_MaxArgs = maxArgs;
		m_AllowDuplicates = allowDuplicates;
		m_ValidFunction = validFunction;
		this.allocateComponents();
	}
	
	private void resizeWindow(){
		Window ancestor = SwingUtilities.getWindowAncestor(this);
		if(ancestor != null){
			ancestor.pack();
		}
	}
	
	private boolean shouldAddData(Data d){
		return this.isValid(d) && (m_AllowDuplicates || !m_SelectedData.contains(d));
	}
	
	private int getNewComponentIndex(){
		return m_DataComps.size();
	}
	
	private boolean canAddComponent(int index){
		return index < m_MaxArgs;
	}
	
	private boolean componentIsOptional(int index){
		return m_MinArgs <= index;
	}
	
	private boolean isRequired(int index){
		return index < m_MinArgs;
	}
	
	private void allocateComponents(){
		while(m_DataComps.size() <= m_MinArgs && m_DataComps.size() < m_MaxArgs){
			this.addDataComponent();
		}
		this.resizeWindow();
	}
	
	private void addDataComponent(){
		int index = this.getNewComponentIndex();
		if(!this.canAddComponent(index)){
			return;
		}
		final DataSelectionPanel comp = new DataSelectionPanel( m_Core, this.componentIsOptional(index) );
		comp.setCloseAction(new DataSelectionPanel.OnClose() {
			@Override
			public void close() {
				DataArgsComponent.this.removeDataComponent(comp);
			}
		});
		
		m_DataComps.add(comp);
		m_SelectedData.add(index, null);
		
		for(Data d : m_Core.getDataManager().getAllData()){
			if(shouldAddData(d)){
				comp.addData(d);
			}
		}
		this.add(comp);
	}
	
	private void removeDataComponent(DataSelectionPanel comp){
		int index = m_DataComps.indexOf(comp);
		if(index >= 0){
			this.remove(comp);
			m_DataComps.remove(index);
			m_SelectedData.remove(index);
			this.allocateComponents();
		}
	}
	
	public List<Data> getDataArgs(){
		List<Data> data = new ArrayList<Data>();
		for(Data d : m_SelectedData){
			if(d != null && this.isValid(d)) data.add(d);
		}
		return data;
	}
	
	private void addData(Data d){
		for(DataSelectionPanel sel : m_DataComps){
			sel.addData(d);
		}
	}
	
	private void removeData(Data d){
		for(DataSelectionPanel sel : m_DataComps){
			sel.removeData(d);
		}
	}
	
	private void renameData(String oldName, String newName){
		for(DataSelectionPanel sel : m_DataComps){
			sel.renameData(oldName, newName);
		}
	}
	
	@Override
	public void update(CoreEvent event) {
		switch(event.type()){
		case DATA_ADDED:
			this.addData(event.getData());
			break;
		case DATA_NAME_CHANGE:
			this.renameData(event.getOldName(), event.getNewName());
			break;
		case DATA_REMOVED:
			this.removeData(event.getData());
			break;
		default:
			break;
		}
	}

	@Override
	public void update(Subject<Data> observed, Data notification) {
		int index = m_DataComps.indexOf(observed);
		if(index >= 0){
			m_SelectedData.set(index, notification);
			this.updateValidity();
		}
	}
	
	private void updateValidity(){
		boolean valid = true;
		for(int i=0 ; i<m_SelectedData.size(); i++){
			Data d = m_SelectedData.get(i);
			if(this.isRequired(i)){
				valid = valid && d != null && this.isValid(d);
			}else{
				valid = valid && (d == null || this.isValid(d));
			}
		}
		this.setValid(valid);
	}
	
	protected void setValid(boolean valid){
		if(m_Valid != valid){
			m_Valid = valid;
			this.notifyListeners();
		}
	}
	
	protected boolean isValid(Data d){
		return m_ValidFunction.isValid(d);
	}
	
	@Override
	public boolean isStateValid() {
		return m_Valid;
	}
	
	protected void notifyListeners(){
		for(ValidListener l : m_Listeners){
			l.update(this, this.isStateValid());
		}
	}

	@Override
	public void addListener(ValidListener l) {
		m_Listeners.add(l);
	}

	@Override
	public void removeListener(ValidListener l) {
		m_Listeners.remove(l);
	}

	
}
