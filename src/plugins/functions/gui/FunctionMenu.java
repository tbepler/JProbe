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
import jprobe.services.function.FunctionPrototype;

public class FunctionMenu extends JMenu implements CoreListener{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore core;
	private Bundle bundle;
	private Map<FunctionPrototype, JMenuItem> items;
	private FunctionDialogHandler functionWindow;
	
	public FunctionMenu(Frame owner, JProbeCore core, Bundle bundle){
		super("Functions");
		this.core = core;
		this.bundle = bundle;
		this.functionWindow = new FunctionDialogHandler(owner, false);
		items = new HashMap<FunctionPrototype, JMenuItem>();
		for(FunctionPrototype f : core.getFunctionManager().getAllFunctionPrototypes()){
			items.put(f, new FunctionMenuItem(core.getDataManager(), bundle, f, functionWindow));
			this.add(items.get(f));
		}
		this.add(new ErrorTest(bundle));
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
			FunctionPrototype f = event.getFunctionPrototype();
			items.put(f, new FunctionMenuItem(core.getDataManager(), bundle, f, functionWindow));
			this.add(items.get(f));
			this.revalidate();
		}
		if(event.type() == CoreEvent.Type.FUNCTION_REMOVED){
			FunctionPrototype f = event.getFunctionPrototype();
			this.remove(items.get(f));
			items.remove(f);
			this.revalidate();
		}
	}
	
	
}
