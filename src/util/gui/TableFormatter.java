package util.gui;

import java.awt.Component;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableFormatter {
	
	public static void formatTable(JTable table){
		formatTable(table, Integer.MAX_VALUE, -1);
	}
	
	public static void formatTable(JTable table, int maxWidth, int rowSampleSize){
		TableColumnModel model = table.getColumnModel();
		Random r = new Random(rowSampleSize);
		for(int i=0; i<table.getColumnCount(); i++){
			TableColumn column = model.getColumn(i);
			int cellWidth;
			if(rowSampleSize > 0){
				cellWidth = preferredCellWidthSampling(table, i, rowSampleSize, r);
			}else{
				cellWidth = preferredCellWidth(table, i);
			}
			int headerWidth = preferredHeaderWidth(table, i);
			int width = Math.max(cellWidth, headerWidth);
			column.setPreferredWidth(Math.min(width, maxWidth));
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
	
	private static int preferredCellWidthSampling(JTable table, int col, int sampleSize, Random r){
		if(table.getRowCount() <= sampleSize){
			return preferredCellWidth(table, col);
		}
		Set<Integer> samples = sampleWithoutReplacement(table.getRowCount(), sampleSize, r);
		int max = 0;
		for(int sample : samples){
			TableCellRenderer render = table.getCellRenderer(sample, col);
			Component comp = table.prepareRenderer(render, sample, col);
			int width = comp.getPreferredSize().width;
			if(width > max) max = width;
		}
		
		return max;
		
	}
	
	private static Set<Integer> sampleWithoutReplacement(int popSize, int sampleSize, Random r){
		Set<Integer> samples = new HashSet<Integer>();
		int[] pop = new int[popSize];
		for(int i=0; i<popSize; i++){
			pop[i] = i;
		}
		while(samples.size() < sampleSize){
			int i = r.nextInt(popSize);
			samples.add(pop[i]);
			swap(pop, i, popSize-1);
			--popSize;
		}
		
		return samples;
	}
	
	private static void swap(int[] array, int i, int j){
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	
	
	
}
