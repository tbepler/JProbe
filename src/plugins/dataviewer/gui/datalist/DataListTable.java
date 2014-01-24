package plugins.dataviewer.gui.datalist;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import plugins.dataviewer.gui.Constants;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataListTable extends JTable implements CoreListener{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore m_Core;
	private DefaultTableModel m_Model;
	
	public DataListTable(JProbeCore core){
		super();
		m_Core = core;
		m_Core.addCoreListener(this);
		m_Model = new DefaultTableModel(new String[][]{}, Constants.DATALIST_COL_HEADERS);
		this.setModel(m_Model);
		
	}
	
	private void add(Data data){
		m_Model.addRow(new String[]{m_Core.getDataManager().getDataName(data), data.getClass().getSimpleName()});
		
	}
	
	@Override
	public void update(CoreEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
