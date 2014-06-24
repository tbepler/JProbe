package jprobe.services.data;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import crossplatform.OS;
import crossplatform.Platform;
import jprobe.Constants;
import jprobe.JProbeActivator;
import jprobe.services.ErrorHandler;
import util.gui.TableFormatter;

public abstract class AbstractFinalData implements Data{
	private static final long serialVersionUID = 1L;
	
	private transient Collection<TableModelListener> m_Listeners = new HashSet<TableModelListener>();
	
	private final int m_Cols;
	private final int m_Rows;
	
	protected AbstractFinalData(int cols, int rows){
		m_Cols = cols;
		m_Rows = rows;
	}
	
	//readObject method to init the transient listeners collection after deserialization
	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException{
		in.defaultReadObject();
		m_Listeners = new HashSet<TableModelListener>();
	}

	@Override
	public int getColumnCount() {
		return m_Cols;
	}

	@Override
	public int getRowCount() {
		return m_Rows;
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		//do nothing
	}

	@Override
	public JTable createTable() {
		JTable table = new JTable(this);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		try{
			((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		}catch(Exception e){
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
		TableFormatter.formatTable(table, Constants.DATA_TABLE_MAX_COL_WIDTH, Constants.DATA_TABLE_ROW_SAMPLE);
		if(Platform.getInstance().getOperatingSystem() == OS.MAC){
			table.setGridColor(Color.BLACK);
			table.setShowGrid(true);
		}
		return table;
	}
	
	
	protected void notifyListeners(TableModelEvent e){
		for(TableModelListener l : m_Listeners){
			l.tableChanged(e);
		}
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		m_Listeners.add(arg0);
	}
	

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		m_Listeners.remove(arg0);
	}

}
