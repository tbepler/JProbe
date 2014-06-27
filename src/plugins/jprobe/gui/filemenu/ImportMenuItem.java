package plugins.jprobe.gui.filemenu;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import plugins.jprobe.gui.ExportImportUtil;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class ImportMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private final Frame m_Parent;
	private final Class<? extends Data> m_ImportClass;
	private final JProbeCore m_Core;
	private final JFileChooser m_FileChooser;
	
	public ImportMenuItem(Frame parent, Class<? extends Data> importClass, JProbeCore core, JFileChooser fileChooser){
		super(importClass.getSimpleName());
		m_Parent = parent;
		m_ImportClass = importClass;
		m_Core = core;
		m_FileChooser = fileChooser;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ExportImportUtil.importData(m_ImportClass, m_Core, m_FileChooser, m_Parent);
	}
	
	
	
}