package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableFormatter {
	
	public enum Method{
		MEDIAN,
		MODE,
		MAX;
	}
	
	public static void format(JTable table, Method method){
		switch(method){
		case MEDIAN:
			break;
		case MODE:
			formatMode(table);
			break;
		case MAX:
			break;
		}
	}
	
	private static void formatMode(JTable table){
		TableColumnModel model = table.getColumnModel();
		for(int i=0; i<table.getColumnCount(); i++){
			TableColumn column = model.getColumn(i);
			int cellWidth = preferredCellModeWidth(table, i);
			int headerWidth = preferredHeaderWidth(table, i);
			table.getTableHeader().setResizingColumn(column);
			column.setWidth(Math.max(cellWidth, headerWidth));
		}
	}
	
	private static int preferredHeaderWidth(JTable table, int col){
		TableColumn column = table.getColumnModel().getColumn(col);
		return (int) table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(
				table,
				column.getIdentifier(),
				false,
				false,
				-1,
				col).getPreferredSize().getWidth();
	}
	
	private static int preferredCellModeWidth(JTable table, int col){
		Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
		counts.put(0,0);
		int mode = 0;
		for(int i=0; i<table.getRowCount(); i++){
			int width = (int) table.getCellRenderer(i, col).getTableCellRendererComponent(
					table, 
					table.getValueAt(i, col),
					false,
					false,
					i,
					col).getPreferredSize().getWidth();
			if(!counts.containsKey(width)){
				counts.put(width, 1);
				if(1 > counts.get(mode) || (1 == counts.get(mode) && width > mode)){
					mode = width;
				}
			}else{
				int count = counts.get(width) + 1;
				counts.put(width, count);
				if(count > counts.get(mode) || (count == counts.get(mode) && width > mode)){
					mode = width;
				}
			}
		}
		return mode + table.getIntercellSpacing().width;
	}
	
	
	
	
	
}
