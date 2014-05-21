package jprobe.services.function.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.BoxLayout;
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
		super(new GridBagLayout());
		m_Core = core;
		m_Core.addCoreListener(this);
		m_MinArgs = minArgs;
		m_MaxArgs = maxArgs;
		m_AllowDuplicates = allowDuplicates;
		m_DataClass = dataClass;
		m_ValidFunction = validFunction;
		this.allocateComponents();
	}
	
	@Override
	public void setEnabled(boolean enabled){
		for(DataSelectionPanel<D> sel : m_DataComps){
			sel.setEnabled(enabled);
		}
	}
	
	private void resizeWindow(){
		Window ancestor = SwingUtilities.getWindowAncestor(this);
		if(ancestor != null){
			ancestor.pack();
		}
	}
	
	protected GridBagConstraints constraints(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		return gbc;
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
		while(m_DataComps.size() < m_MinArgs){
			this.addDataComponent();
		}
		while(m_DataComps.size() < m_MaxArgs){
			int index = m_SelectedData.size() - 1;
			if(index >=0 && m_SelectedData.get(index) == null){
				break;
			}
			this.addDataComponent();
		}
		this.revalidate();
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
		
		m_DataComps.add(index, comp);
		m_SelectedData.add(index, comp.getSelectedData());
		comp.register(this);
		
		for(Data d : m_Core.getDataManager().getAllData()){
			if(shouldAddData(d)){
				comp.addData((D)d);
			}
		}
		this.add(comp, this.constraints());
	}
	
	private void removeDataComponent(DataSelectionPanel<D> comp){
		int index = m_DataComps.indexOf(comp);
		if(index >= 0){
			this.setData(index, null);
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
		for(int i=0; i<m_DataComps.size(); i++){
			DataSelectionPanel<D> sel = m_DataComps.get(i);
			if(this.shouldAddData(d))
				sel.addData(d);
		}
	}
	
	private void removeData(D d){
		for(int i=0; i<m_DataComps.size(); i++){
			DataSelectionPanel<D> sel = m_DataComps.get(i);
			sel.removeData(d);
		}
	}
	
	private void renameData(String oldName, String newName){
		for(int i=0; i<m_DataComps.size(); i++){
			DataSelectionPanel<D> sel = m_DataComps.get(i);
			sel.renameData(oldName, newName);
		}
	}
	
	private void setData(int index, D data){
		D cur = m_SelectedData.get(index);
		m_SelectedData.set(index, data);
		if(!m_AllowDuplicates){
			if(data != null){
				for(int i=0; i<m_DataComps.size(); i++){
					if(i != index)
						m_DataComps.get(i).removeData(data);
				}
			}	
			if(cur != null)
				this.addData(cur);
		}
		this.allocateComponents();
	}
	
	@Override
	public void update(CoreEvent event) {
		Data d;
		switch(event.type()){
		case DATA_ADDED:
			d = event.getData();
			if(this.isValid(d))
				this.addData((D) d);
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
		//System.out.println("Selection changed at index="+index);
		if(index >= 0){
			this.setData(index, notification);
			this.updateValidity();
		}
	}
	
	private void updateValidity(){
		boolean valid = true;
		for(int i=0 ; i<m_SelectedData.size(); i++){
			D d = m_SelectedData.get(i);
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
		//System.out.println("Valid="+m_Valid);
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