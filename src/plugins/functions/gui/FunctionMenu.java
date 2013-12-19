package plugins.functions.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.Function;
import jprobe.services.JProbeCore;

public class FunctionMenu extends JMenu implements CoreListener{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore core;
	private Map<Function, JMenuItem> items;
	
	public FunctionMenu(JProbeCore core){
		super("Functions");
		this.core = core;
		items = new HashMap<Function, JMenuItem>();
		for(Function f : core.getAllFunctions()){
			items.put(f, new FunctionMenuItem(f));
			this.add(items.get(f));
		}
		this.core.addCoreListener(this);
		this.setVisible(true);
		this.setEnabled(true);
	}
	
	void cleanup(){
		core.removeCoreListener(this);
	}

	@Override
	public void update(CoreEvent event) {
		if(event.type() == CoreEvent.Type.FUNCTION_ADDED){
			Function f = event.getFunction();
			items.put(f, new FunctionMenuItem(f));
			this.add(items.get(f));
			this.revalidate();
		}
		if(event.type() == CoreEvent.Type.FUNCTION_REMOVED){
			Function f = event.getFunction();
			this.remove(items.get(f));
			items.remove(f);
			this.revalidate();
		}
	}
	
	
}
