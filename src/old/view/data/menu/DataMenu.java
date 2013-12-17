package old.view.data.menu;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import old.core.Core;
import old.view.CoreMenu;
import old.view.data.DataContext;

public class DataMenu extends CoreMenu{
	private static final long serialVersionUID = 1L;
	
	private CoreMenu read;
	private WriteMenuItem write;
	private JFileChooser readWriteChooser;
	private DataContext context;
	
	public DataMenu(Core core, DataContext context) {
		super("Data", core);
		this.context = context;
		readWriteChooser = new JFileChooser("");
		read = new ReadMenu(core, readWriteChooser);
		write = new WriteMenuItem(readWriteChooser, core, context);
		this.add(read);
		this.add(write);
	}

	@Override
	public void updateMenu() {
		read.updateMenu();
		write.update(context);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
