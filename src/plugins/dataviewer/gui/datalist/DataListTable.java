package plugins.dataviewer.gui.datalist;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import plugins.dataviewer.gui.Constants;
import jprobe.services.JProbeCore;

public class DataListTable extends JTable{
	private static final long serialVersionUID = 1L;
	
	DataListModel m_Model;
	
	public DataListTable(JProbeCore core){
		super();
		m_Model = new DataListModel(core, SwingUtilities.getWindowAncestor(this));
		this.setModel(m_Model);
		this.setDragEnabled(false);
		for(int i=0; i<this.getColumnCount(); i++){
			this.getColumnModel().getColumn(i).setMinWidth(Constants.DATALIST_MIN_COL_WIDTH);
		}
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setPreferredScrollableViewportSize(this.getPreferredSize());
		this.revalidate();
	}
	
	public void cleanup(){
		m_Model.cleanup();
	}
	
	
	
}
