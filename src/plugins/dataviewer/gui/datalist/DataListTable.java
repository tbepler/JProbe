package plugins.dataviewer.gui.datalist;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import jprobe.services.JProbeCore;

public class DataListTable extends JTable{
	private static final long serialVersionUID = 1L;
	
	DataListModel m_Model;
	
	public DataListTable(JProbeCore core){
		super();
		m_Model = new DataListModel(core, SwingUtilities.getWindowAncestor(this));
		this.setModel(m_Model);
		this.setDragEnabled(false);
		
	}
	
	public void cleanup(){
		m_Model.cleanup();
	}
	
	
	
}
