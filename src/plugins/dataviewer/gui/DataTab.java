package plugins.dataviewer.gui;

import java.awt.Component;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

import jprobe.services.data.Data;

public class DataTab extends JScrollPane{
	private static final long serialVersionUID = 1L;
	

	class RowHeaderRenderer extends JLabel implements ListCellRenderer<Integer> {
		private static final long serialVersionUID = 1L;

		RowHeaderRenderer(JTable table) {
			JTableHeader header = table.getTableHeader();
			setOpaque(true);
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setHorizontalAlignment(CENTER);
			setForeground(header.getForeground());
			setBackground(header.getBackground());
			setFont(header.getFont());
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends Integer> list, Integer value, int index, boolean isSelected, boolean cellHasFocus) {
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	public class DataHeader extends JList<Integer> {
		private static final long serialVersionUID = 1L;

		public DataHeader(final Data data, JTable table) {
			super(new AbstractListModel<Integer>(){
				private static final long serialVersionUID = 1L;
				
				int[] headers = this.generateHeader(data);
				
				private int[] generateHeader(Data data){
					int[] h = new int[data.getRowCount()];
					for(int i=1; i<=h.length; i++){
						h[i-1] = i;
					}
					return h;
				}
				
				@Override
				public int getSize(){
					return headers.length;
				}
				
				@Override
				public Integer getElementAt(int index) {
					return headers[index];
				}
			});
			
			this.setFixedCellWidth(50);

			this.setFixedCellHeight(table.getRowHeight());
			//		+ table.getRowMargin());
			//                           + table.getIntercellSpacing().height);
			this.setCellRenderer(new RowHeaderRenderer(table));

		}
	}
	
	private Data m_Data;
	
	public DataTab(Data data){
		super();
		JTable table = data.createTable();
		this.setViewportView(table);
		m_Data = data;
		this.setRowHeaderView(new DataHeader(data, table));
	}
	
	public Data getData(){
		return m_Data;
	}
	
}
