package plugins.functions.gui.dialog;

import java.awt.Component;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import plugins.functions.gui.Constants;
import plugins.functions.gui.dialog.data.DataPanel;
import plugins.functions.gui.dialog.field.FieldPanel;
import plugins.functions.gui.utils.StateListener;
import plugins.functions.gui.utils.StateNotifier;
import plugins.functions.gui.utils.ValidStateNotifier;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;

public class ParameterPanel extends JPanel implements ValidStateNotifier, StateListener{
	private static final long serialVersionUID = 1L;
	
	private FieldPanel m_FieldPanel;
	private DataPanel m_DataPanel;
	private boolean m_Valid;
	private Collection<StateListener> m_Listeners = new HashSet<StateListener>();
	
	public ParameterPanel(DataParameter[] dataParams, FieldParameter[] fieldParams, JProbeCore core){
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		if(dataParams!=null && dataParams.length>0){
			m_DataPanel = new DataPanel(dataParams, core);
			m_DataPanel.addStateListener(this);
			this.add(this.createBorderedPanel(Constants.DATAPANEL_TITLE, m_DataPanel));
		}else{
			m_DataPanel = null;
		}
		if(fieldParams!=null && fieldParams.length>0){
			m_FieldPanel = new FieldPanel(fieldParams);
			m_FieldPanel.addStateListener(this);
			this.add(this.createBorderedPanel(Constants.FIELDPANEL_TITLE, m_FieldPanel));
		}else{
			m_FieldPanel = null;
		}
		m_Valid = this.componentsValid();
		
	}
	
	private JPanel createBorderedPanel(String title, Component content){
		JPanel bordered = new JPanel();
		bordered.setBorder(BorderFactory.createTitledBorder(title));
		bordered.add(content);
		return bordered;
	}
	
	public Data[] getDataArgs(){
		if(m_DataPanel == null){
			return new Data[]{};
		}
		return m_DataPanel.getDataArgs();
	}
	
	public Field[] getFieldArgs(){
		if(m_FieldPanel == null){
			return new Field[]{};
		}
		return m_FieldPanel.getFieldArgs();
	}
	
	private boolean componentsValid(){
		return this.isDataValid() && this.isFieldValid();
	}
	
	private boolean isDataValid(){
		return m_DataPanel == null || m_DataPanel.isStateValid();
	}
	
	private boolean isFieldValid(){
		return m_FieldPanel == null || m_FieldPanel.isStateValid();
	}

	@Override
	public void addStateListener(StateListener l) {
		m_Listeners.add(l);
	}

	@Override
	public void removeStateListener(StateListener l) {
		m_Listeners.remove(l);
	}
	
	private void notifyListeners(){
		for(StateListener l : m_Listeners){
			l.update(this);
		}
	}

	@Override
	public boolean isStateValid() {
		return m_Valid;
	}

	@Override
	public void update(StateNotifier source) {
		boolean newState = this.componentsValid();
		if(m_Valid != newState){
			m_Valid = newState;
			this.notifyListeners();
		}
	}
	
}
