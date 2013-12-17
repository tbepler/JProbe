package old.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import old.core.Core;
import old.core.exceptions.ModuleExecutionException;
import old.core.exceptions.NoSuchModuleException;

public class ModuleMenu extends CoreMenu{
	private static final long serialVersionUID = 1L;
	
	public ModuleMenu(Core core){
		super("Modules", core);
		this.updateMenu();
	}
	
	public void updateMenu(){
		this.removeAll();
		for(String mod : core.getModuleNames()){
			JMenuItem item = new JMenuItem(mod);
			item.setToolTipText(core.getModuleDescription(mod));
			item.setActionCommand(mod);
			item.addActionListener(this);
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
