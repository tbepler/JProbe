package plugins.jprobe.gui.filemenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import plugins.jprobe.gui.GUIActivator;
import jprobe.services.ErrorHandler;
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
		//retrieve the registered file extension filters
		FileNameExtensionFilter[] formats = m_Core.getDataManager().getValidReadFormats(m_ImportClass);
		//if there are none, then there is an error in the DataReader, so warn the user and return
		if(formats.length <= 0){
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Warning: there are no readable formats for this data type.", "Import Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		//set the file chooser's file filters to those retrieved above
		m_FileChooser.resetChoosableFileFilters();
		m_FileChooser.setAcceptAllFileFilterUsed(false);
		for(FileNameExtensionFilter format : formats){
			m_FileChooser.addChoosableFileFilter(format);
		}
		//show the file chooser and read data from the selected file using the selected file format
		int returnVal = m_FileChooser.showDialog(SwingUtilities.getWindowAncestor(this), "Import");
		if(returnVal == JFileChooser.APPROVE_OPTION){
			try {
				FileNameExtensionFilter selectedFormat = (FileNameExtensionFilter) m_FileChooser.getFileFilter();
				m_Core.getDataManager().readData(m_FileChooser.getSelectedFile(), m_ImportClass, selectedFormat, GUIActivator.getBundle());
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
			}
		}
	}
	
	
	
}