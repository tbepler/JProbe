package plugins.dataviewer.gui.datalist;

import java.awt.event.ActionEvent;

import jprobe.services.data.Data;
import plugins.dataviewer.gui.DataTabPane;

public class ViewDataMenuItem extends AbstractDataMenuItem{
	private static final long serialVersionUID = 1L;

	private DataTabPane m_TabPane;
	
	public ViewDataMenuItem(DataTabPane tabPane, Data data){
		super("View",  data);
		m_TabPane = tabPane;
	}
	
	public ViewDataMenuItem(DataTabPane tabPane){
		this(tabPane, null);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(this.getData() != null){
			m_TabPane.selectData(this.getData());
		}
	}
	
}
