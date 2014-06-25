package plugins.dataviewer.gui.datalist;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import plugins.dataviewer.gui.Constants;
import plugins.dataviewer.gui.DataTabPane;
import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class DataListTable extends JTable implements MouseListener{
	private static final long serialVersionUID = 1L;
	
	DataListModel m_Model;
	DataPopupMenu m_PopupMenu;
	
	public DataListTable(JProbeCore core, JProbeGUI gui, DataTabPane tabPane){
		super();
		m_PopupMenu = new DataPopupMenu(core, gui, tabPane);
		m_Model = new DataListModel(core);
		this.setModel(m_Model);
		this.setDragEnabled(false);
		this.setShowGrid(true);
		for(int i=0; i<this.getColumnCount(); i++){
			this.getColumnModel().getColumn(i).setMinWidth(Constants.DATALIST_MIN_COL_WIDTH);
		}
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.setPreferredScrollableViewportSize(this.getPreferredSize());
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
	public void mouseReleased(MouseEvent event) {
		if(event.getButton() == MouseEvent.BUTTON3){
			int row = this.rowAtPoint(event.getPoint());
			if(row < 0 || row >= this.getRowCount()){
				return;
			}
			Data selected = m_Model.getData(row);
			if(selected != null){
				this.setRowSelectionInterval(row, row);
				m_PopupMenu.setData(selected);
				m_PopupMenu.show(this, event.getX(), event.getY());
			}
		}
	}
	
	
	
}
