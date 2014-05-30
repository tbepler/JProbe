package plugins.jprobe.gui.filemenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import plugins.jprobe.gui.ExportImportUtil;
import plugins.jprobe.gui.GUIActivator;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class ImportMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Class<? extends Data> m_ImportClass;
	private JProbeCore m_Core;
	private JFileChooser m_FileChooser;
	
	public ImportMenuItem(Class<? extends Data> importClass, JProbeCore core, JFileChooser fileChooser){
		super(importClass.getSimpleName());
		m_ImportClass = importClass;
		m_Core = core;
		m_FileChooser = fileChooser;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ExportImportUtil.importData(m_ImportClass, m_Core, m_FileChooser, GUIActivator.getFrame());
	}
	
	
	
}