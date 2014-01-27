package plugins.jprobe.gui.filemenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import plugins.jprobe.gui.GUIActivator;
import jprobe.services.ErrorHandler;
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
		//retreive file formats for this data object
		FileNameExtensionFilter[] formats = m_Core.getDataManager().getValidWriteFormats(m_Data.getClass());
		//if there are none, then there is an error in the data writer. warn the user and return
		if(formats.length <= 0){
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Warning: there are no writable formats for this data type.", "Export Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		//set the file chooser's file filters to those retreived above
		m_FileChooser.resetChoosableFileFilters();
		m_FileChooser.setAcceptAllFileFilterUsed(false);
		for(FileNameExtensionFilter format : formats){
			m_FileChooser.addChoosableFileFilter(format);
		}
		//show the file chooser and write the data to the selected file using the selected file format
		int returnVal = m_FileChooser.showDialog(SwingUtilities.getWindowAncestor(this), "Export");
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = m_FileChooser.getSelectedFile();
			try {
				FileNameExtensionFilter format = (FileNameExtensionFilter) m_FileChooser.getFileFilter();
				m_Core.getDataManager().writeData(file, m_Data, format);
			} catch (Exception e) {
				ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
			}
		}
	}
	
	
	
	
}
