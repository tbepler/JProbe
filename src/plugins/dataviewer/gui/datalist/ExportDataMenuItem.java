package plugins.dataviewer.gui.datalist;

import java.awt.event.ActionEvent;

import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;


public class ExportDataMenuItem extends AbstractDataMenuItem{
	private static final long serialVersionUID = 1L;
	
	private JProbeGUI m_Gui;
	private JProbeCore m_Core;
	
	public ExportDataMenuItem(JProbeCore core, JProbeGUI gui) {
		this(core, gui, null);
	}
	
	public ExportDataMenuItem(JProbeCore core, JProbeGUI gui, Data data){
		super("Export", data);
		m_Gui = gui;
		m_Core = core;
		if(data == null){
			this.setVisible(false);
		}else{
			this.setVisible(m_Core.getDataManager().isWritable(data.getClass()));
		}
	}
	
	@Override
	public void setData(Data data){
		super.setData(data);
		if(data == null){
			this.setVisible(false);
		}else{
			this.setVisible(m_Core.getDataManager().isWritable(data.getClass()));
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(this.getData() != null){
			m_Gui.write(this.getData());
		}
	}
	
	
	
}
