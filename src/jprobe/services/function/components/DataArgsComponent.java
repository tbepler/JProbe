package jprobe.services.function.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Observer;
import util.Subject;
import util.gui.ChangeNotifier;
import jprobe.services.Workspace;
import jprobe.services.WorkspaceEvent;
import jprobe.services.WorkspaceListener;
import jprobe.services.data.Data;

public class DataArgsComponent<D extends Data> extends JPanel implements ChangeNotifier, Observer<D>, WorkspaceListener{
	private static final long serialVersionUID = 1L;
	
	public static interface DataValidFunction{
		public boolean isValid(Data d);
	}
	
	private final Collection<ChangeListener> m_Listeners = new HashSet<ChangeListener>();
	
	private final List<DataSelectionPanel<D>> m_DataComps = new ArrayList<DataSelectionPanel<D>>();
	private final List<D> m_SelectedData = new ArrayList<D>();
	
	private final Workspace m_Workspace;
	private final int m_MinArgs;
	private final int m_MaxArgs;
	private final boolean m_AllowDuplicates;
	private final Class<D> m_DataClass;
	private final DataValidFunction m_ValidFunction;
	
	public DataArgsComponent(Workspace w, int minArgs, int maxArgs, boolean allowDuplicates, Class<D> dataClass, DataValidFunction validFunction){
		super(new GridBagLayout());
		m_Workspace = w;
		m_Workspace.addWorkspaceListener(this);
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
		super.setEnabled(enabled);
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
		this.invalidate();
		this.validate();
		this.resizeWindow();
	}
	
	private void clear(){
		for(DataSelectionPanel<D> p : m_DataComps){
			p.unregister(this);
		}
		this.removeAll();
		m_DataComps.clear();
		m_SelectedData.clear();
		this.allocateComponents();
	}
	
	protected DataSelectionPanel<D> newDataSelectionPanel(boolean optional){
		return new DataSelectionPanel<D>( optional );
	}
	
	protected void initSelectionPanel(final DataSelectionPanel<D> comp){
		comp.setCloseAction(new DataSelectionPanel.OnClose() {
			@Override
			public void close() {
				DataArgsComponent.this.removeDataComponent(comp);
			}
		});
		
		comp.register(this);
		
		for(Data d : m_Workspace.getAllData()){
			if(m_DataClass.isAssignableFrom(d.getClass()) && shouldAddData(d)){
				comp.addData(m_DataClass.cast(d), m_Workspace.getDataName(d));
			}
		}
		
		comp.setEnabled(this.isEnabled());
	}
	
	private void addDataComponent(){
		int index = this.getNewComponentIndex();
		if(!this.canAddComponent(index)){
			return;
		}
		DataSelectionPanel<D> comp = this.newDataSelectionPanel(this.componentIsOptional(index));
		this.initSelectionPanel(comp);
		
		m_DataComps.add(index, comp);
		m_SelectedData.add(index, comp.getSelectedData());
	
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
	
	protected List<DataSelectionPanel<D>> getSelectionComps(){
		return Collections.unmodifiableList(m_DataComps);
	}
	
	private void addData(D d){
		for(int i=0; i<m_DataComps.size(); i++){
			DataSelectionPanel<D> sel = m_DataComps.get(i);
			if(this.shouldAddData(d)){
				sel.addData(d, m_Workspace.getDataName(d));
			}
		}
	}
	
	private void removeData(D d){
		for(int i=0; i<m_DataComps.size(); i++){
			DataSelectionPanel<D> sel = m_DataComps.get(i);
			sel.removeData(d);
		}
	}
	
	private void renameData(D data, String newName){
		for(int i=0; i<m_DataComps.size(); i++){
			DataSelectionPanel<D> sel = m_DataComps.get(i);
			sel.renameData(data, newName);
		}
	}
	
	private void setData(int index, D data){
		D cur = m_SelectedData.get(index);
		m_SelectedData.set(index, data);
		if(!m_AllowDuplicates){
			if(data != null){
				for(int i=0; i<m_DataComps.size(); i++){
					if(i != index){
						m_DataComps.get(i).removeData(data);
					}
				}
			}	
			if(cur != null){
				this.addData(cur);
			}
		}
		this.allocateComponents();
		this.fireChanged();
	}
	
	protected void process(WorkspaceEvent event){
		D data;
		if(event.data != null && this.isValid(event.data) && m_DataClass.isAssignableFrom(event.data.getClass())){
			data = m_DataClass.cast(event.data);
		}else{
			data = null;
		}
		switch(event.type){
		case DATA_ADDED:
			if(data != null){
				this.addData(m_DataClass.cast(data));
			}
			break;
		case DATA_RENAMED:
			if(data != null){
				this.renameData(data, m_Workspace.getDataName(data));
			}
			break;
		case DATA_REMOVED:
			if(data != null){
				this.removeData(data);
			}
			break;
		case WORKSPACE_CLEARED:
			this.clear();
			break;
		case WORKSPACE_LOADED:
			for(Data d : m_Workspace.getAllData()){
				if(isValid(d) && m_DataClass.isAssignableFrom(d.getClass())){
					this.addData(m_DataClass.cast(d));
				}
			}
		default:
			break;
		}
	}
	
	@Override
	public void update(Workspace source, final WorkspaceEvent event) {
		if(source == m_Workspace){
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					process(event);
				}

			});
		}
	}

	@Override
	public void update(Subject<D> observed, D notification) {
		int index = m_DataComps.indexOf(observed);
		if(index >= 0){
			this.setData(index, notification);
		}
	}
	
	protected boolean isValid(Data d){
		return m_DataClass.isAssignableFrom(d.getClass()) && m_ValidFunction.isValid(d);
	}
	
	protected void fireChanged(){
		ChangeEvent event = new ChangeEvent(this);
		for(ChangeListener l : m_Listeners){
			l.stateChanged(event);;
		}
	}

	@Override
	public void addChangeListener(ChangeListener l) {
		m_Listeners.add(l);
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
		m_Listeners.remove(l);
	}

	
}
