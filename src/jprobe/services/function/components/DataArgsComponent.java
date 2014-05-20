package jprobe.services.function.components;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import util.Observer;
import util.Subject;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataArgsComponent<D extends Data> extends JPanel implements ValidNotifier, Observer<D>, CoreListener{
	private static final long serialVersionUID = 1L;
	
	public static interface DataValidFunction{
		public boolean isValid(Data d);
	}
	
	private final Collection<ValidListener> m_Listeners = new HashSet<ValidListener>();
	
	private final List<DataSelectionPanel<D>> m_DataComps = new ArrayList<DataSelectionPanel<D>>();
	private final List<D> m_SelectedData = new ArrayList<D>();
	
	private final JProbeCore m_Core;
	private final int m_MinArgs;
	private final int m_MaxArgs;
	private final boolean m_AllowDuplicates;
	private final Class<D> m_DataClass;
	private final DataValidFunction m_ValidFunction;
	
	private boolean m_Valid;
	
	public DataArgsComponent(JProbeCore core, int minArgs, int maxArgs, boolean allowDuplicates, Class<D> dataClass, DataValidFunction validFunction){
		super();
		m_Core = core;
		m_Core.addCoreListener(this);
		m_MinArgs = minArgs;
		m_MaxArgs = maxArgs;
		m_AllowDuplicates = allowDuplicates;
		m_DataClass = dataClass;
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
		final DataSelectionPanel<D> comp = new DataSelectionPanel<D>( m_Core, this.componentIsOptional(index) );
		comp.setCloseAction(new DataSelectionPanel.OnClose() {
			@Override
			public void close() {
				DataArgsComponent.this.removeDataComponent(comp);
			}
		});
		
		m_DataComps.add(comp);
		m_SelectedData.add(index, null);
		comp.register(this);
		
		for(Data d : m_Core.getDataManager().getAllData()){
			if(shouldAddData(d)){
				comp.addData((D)d);
			}
		}
		this.add(comp);
	}
	
	private void removeDataComponent(DataSelectionPanel<D> comp){
		int index = m_DataComps.indexOf(comp);
		if(index >= 0){
			this.remove(comp);
			m_DataComps.remove(index);
			m_SelectedData.remove(index);
			this.allocateComponents();
		}
	}
	
	public List<D> getDataArgs(){
		List<D> data = new ArrayList<D>();
		for(D d : m_SelectedData){
			if(d != null && this.isValid(d)) data.add(d);
		}
		return data;
	}
	
	private void addData(D d){
		for(DataSelectionPanel<D> sel : m_DataComps){
			sel.addData(d);
		}
	}
	
	private void removeData(D d){
		for(DataSelectionPanel<D> sel : m_DataComps){
			sel.removeData(d);
		}
	}
	
	private void renameData(String oldName, String newName){
		for(DataSelectionPanel<D> sel : m_DataComps){
			sel.renameData(oldName, newName);
		}
	}
	
	@Override
	public void update(CoreEvent event) {
		Data d;
		switch(event.type()){
		case DATA_ADDED:
			d = event.getData();
			if(this.shouldAddData(d)){
				this.addData((D) d);
			}
			break;
		case DATA_NAME_CHANGE:
			this.renameData(event.getOldName(), event.getNewName());
			break;
		case DATA_REMOVED:
			d = event.getData();
			if(this.isValid(d)){
				this.removeData((D)d);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void update(Subject<D> observed, D notification) {
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
		return m_DataClass.isAssignableFrom(d.getClass()) && m_ValidFunction.isValid(d);
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
