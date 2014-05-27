package jprobe.services.data;

import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public interface Data extends TableModel, Serializable{
	
	public JTable createTable();
	
}
