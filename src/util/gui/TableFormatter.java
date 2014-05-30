package util.gui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableFormatter {
	
	public static void formatTable(JTable table){
		TableColumnModel model = table.getColumnModel();
		for(int i=0; i<table.getColumnCount(); i++){
			TableColumn column = model.getColumn(i);
			int cellWidth = preferredCellWidth(table, i);
			int headerWidth = preferredHeaderWidth(table, i);
			column.setPreferredWidth(Math.max(cellWidth, headerWidth));
		}
	}
	
	private static int preferredHeaderWidth(JTable table, int col){
		TableColumn column = table.getColumnModel().getColumn(col);
		TableCellRenderer render = column.getHeaderRenderer();
		if(render == null){
			render = table.getTableHeader().getDefaultRenderer();
		}
		Component comp = render.getTableCellRendererComponent(
				table,
				column.getHeaderValue(),
				false,
				false,
				-1,
				col);
		return comp.getPreferredSize().width + table.getIntercellSpacing().width * 2;
	}
	
	private static int preferredCellWidth(JTable table, int col){
		int max = 0;
		for(int i=0; i<table.getRowCount(); i++){
			TableCellRenderer render = table.getCellRenderer(i, col);
			Component comp = table.prepareRenderer(render, i, col);
			int width = comp.getPreferredSize().width;
			if(width > max) max = width;
		}
		return max;
	}
	
	
	
	
	
}
