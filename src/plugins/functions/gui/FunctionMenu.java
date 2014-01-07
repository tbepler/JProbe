package plugins.functions.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.osgi.framework.Bundle;

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.DataManager;
import jprobe.services.Function;
import jprobe.services.FunctionManager;
import jprobe.services.JProbeCore;

public class FunctionMenu extends JMenu implements CoreListener{
	private static final long serialVersionUID = 1L;
	
	private FunctionManager functionManager;
	private DataManager dataManager;
	private Bundle bundle;
	private Map<Function, JMenuItem> items;
	
	public FunctionMenu(FunctionManager functionManager, DataManager dataManager, Bundle bundle){
		super("Functions");
		this.functionManager = functionManager;
		this.dataManager = dataManager;
		this.bundle = bundle;
		items = new HashMap<Function, JMenuItem>();
		for(Function f : functionManager.getAllFunctions()){
			items.put(f, new FunctionMenuItem(dataManager, bundle, f));
			this.add(items.get(f));
		}
		this.functionManager.addListener(this);
		this.setVisible(true);
		this.setEnabled(true);
	}
	
	void cleanup(){
		functionManager.removeListener(this);
	}

	@Override
	public void update(CoreEvent event) {
		if(event.type() == CoreEvent.Type.FUNCTION_ADDED){
			Function f = event.getFunction();
			items.put(f, new FunctionMenuItem(dataManager, bundle, f));
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
