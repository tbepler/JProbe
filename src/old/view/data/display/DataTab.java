package old.view.data.display;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import old.datatypes.DataType;

public class DataTab extends JScrollPane{
	private static final long serialVersionUID = 1L;
	
	public final DataType data;
	private JTable table;
	
	public DataTab(DataType data){
		this.data = data;
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.add(table);
		
	}
	
}
