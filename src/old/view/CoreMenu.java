package old.view;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import old.core.Core;

public abstract class CoreMenu extends JMenu implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	protected Core core;
	protected List<JMenuItem> items;
	
	public CoreMenu(String name, Core core){
		super(name);
		this.core = core;
		this.items = new ArrayList<JMenuItem>();
	}
	
	public abstract void updateMenu();
	
	
}
