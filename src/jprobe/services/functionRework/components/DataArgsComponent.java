package jprobe.services.functionRework.components;

import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataArgsComponent extends JPanel implements ValidNotifier, ItemListener, CoreListener{
	private static final long serialVersionUID = 1L;
	
	public static interface DataValidFunction{
		public boolean isValid(Data d);
	}
	
	private final Collection<ValidListener> m_Listeners = new HashSet<ValidListener>();
	
	private final List<DataComboBox> m_DataComps = new ArrayList<DataComboBox>();
	private final Collection<Data> m_SelectedData = new HashSet<Data>();
	
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
	}
	
	private void resizeWindow(){
		Window ancestor = SwingUtilities.getWindowAncestor(this);
		if(ancestor != null){
			ancestor.pack();
		}
	}
	
	private boolean shouldAddData(Data d){
		return m_AllowDuplicates || !m_SelectedData.contains(d);
	}
	
	private int getNewComponentIndex(){
		return m_DataComps.size();
	}
	
	private boolean componentIsOptional(int index){
		return m_MinArgs 
	}
	
	private void addDataComponent(){
		DataComboBox comp = new DataComboBox(m_Core);
		comp.addItemListener(this);
		int index = this.getNewComponentIndex();
		
		for(Data d : m_Core.getDataManager().getAllData()){
			if(shouldAddData(d)){
				comp.addData(d);
			}
		}
		m_DataComps.add(comp);
		this.add(comp);
		this.resizeWindow();
	}
	
	public List<Data> getDataArgs(){
		List<Data> data = new ArrayList<Data>();
		for(DataComboBox box : m_DataComps){
			data.add(box.getSelectedData());
		}
		return data;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void update(CoreEvent event) {
		// TODO Auto-generated method stub
		
	}

	
}
