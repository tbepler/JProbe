package old.view.data.menu;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import old.core.Core;
import old.core.exceptions.DataReadException;
import old.view.CoreMenu;
import old.view.FormatFilter;

public class ReadMenu extends CoreMenu{
	private static final long serialVersionUID = 1L;
	
	private JFileChooser fc;
	
	public ReadMenu(Core core, JFileChooser fc) {
		super("Read", core);
		this.fc = fc;
		this.updateMenu();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String dataType = e.getActionCommand();
		Map<String, String[]> fileFormats = core.getReadableFormats(dataType);
		if(fileFormats == null){
			return;
		}
		fc.resetChoosableFileFilters();
		fc.setAcceptAllFileFilterUsed(false);
		for(String name : fileFormats.keySet()){
			FileFilter f = new FormatFilter(name, fileFormats.get(name));
			fc.addChoosableFileFilter(f);
		}
		int val = fc.showOpenDialog(this);
		if(val == JFileChooser.APPROVE_OPTION){
			FormatFilter chosen = (FormatFilter) fc.getFileFilter();
			try {
				core.read(dataType, fc.getSelectedFile(), chosen.format);
			} catch (DataReadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void updateMenu() {
		this.removeAll();
		for(String readable : core.getReadableDataTypes()){
			JMenuItem i = new JMenuItem(readable);
			i.setActionCommand(readable);
			i.addActionListener(this);
			this.add(i);
		}
	}

}
