package plugins.jprobe.gui.filemenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import plugins.jprobe.gui.ExportImportUtil;
import plugins.jprobe.gui.GUIActivator;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class ExportMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;

	private Data m_Data;
	private JFileChooser m_FileChooser;
	private JProbeCore m_Core;
	
	public ExportMenuItem(Data data, JProbeCore core, JFileChooser fileChooser){
		super(core.getDataManager().getDataName(data));
		m_Data = data;
		m_Core = core;
		m_FileChooser = fileChooser;
		this.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ExportImportUtil.exportData(m_Data, m_Core, m_FileChooser, GUIActivator.getFrame());
	}
	
	
	
	
}
