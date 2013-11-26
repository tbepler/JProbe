package view;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import core.Core;

public class DataMenu extends CoreMenu{
	private static final long serialVersionUID = 1L;
	
	private CoreMenu read;
	private JMenu write;
	private JFileChooser readWriteChooser;
	
	public DataMenu(Core core) {
		super("Data", core);
		readWriteChooser = new JFileChooser("");
		read = new ReadMenu(core, readWriteChooser);
		write = new JMenu("Write");
		this.add(read);
		this.add(write);
	}

	@Override
	public void updateMenu() {
		read.updateMenu();
		write.removeAll();
		for(String writable : core.getWritableDataTypes()){
			JMenuItem i = new JMenuItem(writable);
			i.setActionCommand(writable);
			i.addActionListener(this);
			write.add(i);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
