package plugins.dataviewer.gui.datalist;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;

import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import plugins.dataviewer.gui.DataTabPane;

public class DataPopupMenu extends JPopupMenu{
	private static final long serialVersionUID = 1L;

	private List<AbstractDataMenuItem> m_MenuItems;
	
	public DataPopupMenu(JProbeCore core, DataTabPane tabPane, Data data){
		super();
		m_MenuItems = new ArrayList<AbstractDataMenuItem>();
		this.initMenuItems(core, tabPane, data, m_MenuItems);
		for(AbstractDataMenuItem item : m_MenuItems){
			this.add(item);
		}
	}
	
	public DataPopupMenu(JProbeCore core, DataTabPane tabPane){
		this(core, tabPane, null);
	}
	
	private void initMenuItems(final JProbeCore core, final DataTabPane tabPane, final Data data, List<AbstractDataMenuItem> items){
		items.add(new ViewDataMenuItem(tabPane, data));
		items.add(new DeleteDataMenuItem(core, data));
	}
	
	public void setData(Data data){
		for(AbstractDataMenuItem item : m_MenuItems){
			item.setData(data);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
