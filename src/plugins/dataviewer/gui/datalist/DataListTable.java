package plugins.dataviewer.gui.datalist;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import plugins.dataviewer.gui.Constants;
import plugins.dataviewer.gui.DataTabPane;
import jprobe.services.JProbeCore;

public class DataListTable extends JTable implements MouseListener{
	private static final long serialVersionUID = 1L;
	
	DataListModel m_Model;
	
	public DataListTable(JProbeCore core, DataTabPane tabPane){
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
		this.addMouseListener(this);
	}
	
	public void cleanup(){
		m_Model.cleanup();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		//do nothing
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		//do nothing
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		//do nothing
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		//do nothing
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
