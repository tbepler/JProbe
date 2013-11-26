package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import core.Core;
import exceptions.ModuleExecutionException;
import exceptions.NoSuchModuleException;

public class ModuleMenu extends JMenu implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Core core;
	private List<JMenuItem> items;
	
	public ModuleMenu(Core core){
		super("Modules");
		this.core = core;
		this.items = new ArrayList<JMenuItem>();
		this.updateMenu();
	}
	
	public void updateMenu(){
		for(JMenuItem i : items){
			this.remove(i);
		}
		items.clear();
		for(String mod : core.getModuleNames()){
			JMenuItem item = new JMenuItem(mod);
			item.setToolTipText(core.getModuleDescription(mod));
			item.setActionCommand(mod);
			item.addActionListener(this);
			items.add(item);
			this.add(item);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			core.runModule(e.getActionCommand());
		} catch (NoSuchModuleException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ModuleExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
