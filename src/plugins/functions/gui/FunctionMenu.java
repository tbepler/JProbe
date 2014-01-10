package plugins.functions.gui;

import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.osgi.framework.Bundle;

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.DataManager;
import jprobe.services.FunctionManager;
import jprobe.services.JProbeCore;
import jprobe.services.function.Function;

public class FunctionMenu extends JMenu implements CoreListener{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore core;
	private Bundle bundle;
	private Map<Function, JMenuItem> items;
	private FunctionDialogHandler functionWindow;
	
	public FunctionMenu(Frame owner, JProbeCore core, Bundle bundle){
		super("Functions");
		this.core = core;
		this.bundle = bundle;
		this.functionWindow = new FunctionDialogHandler(owner, false);
		items = new HashMap<Function, JMenuItem>();
		for(Function f : core.getFunctionManager().getAllFunctions()){
			items.put(f, new FunctionMenuItem(core.getDataManager(), bundle, f, functionWindow));
			this.add(items.get(f));
		}
		this.add(new ErrorTest(bundle, core.getErrorHandler()));
		this.core.getFunctionManager().addListener(this);
		this.setVisible(true);
		this.setEnabled(true);
	}
	
	void cleanup(){
		core.getFunctionManager().removeListener(this);
	}

	@Override
	public void update(CoreEvent event) {
		if(event.type() == CoreEvent.Type.FUNCTION_ADDED){
			Function f = event.getFunction();
			items.put(f, new FunctionMenuItem(core.getDataManager(), bundle, f, functionWindow));
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
