package plugins.functions.gui;

import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.osgi.framework.Bundle;

import plugins.functions.gui.dialog.FunctionDialogHandler;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.function.Function;

public class FunctionMenu extends JMenu implements CoreListener{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore m_Core;
	private Bundle m_Bundle;
	private Map<Function, JMenuItem> m_Items;
	private FunctionDialogHandler m_FunctionWindow;
	
	public FunctionMenu(Frame owner, JProbeCore core, Bundle bundle){
		super("Functions");
		m_Core = core;
		m_Bundle = bundle;
		m_FunctionWindow = new FunctionDialogHandler(owner, false);
		m_Items = new HashMap<Function, JMenuItem>();
		for(Function f : core.getFunctionManager().getAllFunctions()){
			m_Items.put(f, new FunctionMenuItem(core, bundle, f, m_FunctionWindow));
			this.add(m_Items.get(f));
		}
		this.add(new ErrorTest(bundle));
		m_Core.getFunctionManager().addListener(this);
		this.setVisible(true);
		this.setEnabled(true);
	}
	
	void cleanup(){
		m_Core.getFunctionManager().removeListener(this);
	}

	@Override
	public void update(CoreEvent event) {
		if(event.type() == CoreEvent.Type.FUNCTION_ADDED){
			Function f = event.getFunction();
			m_Items.put(f, new FunctionMenuItem(m_Core, m_Bundle, f, m_FunctionWindow));
			this.add(m_Items.get(f));
			this.revalidate();
		}
		if(event.type() == CoreEvent.Type.FUNCTION_REMOVED){
			Function f = event.getFunction();
			this.remove(m_Items.get(f));
			m_Items.remove(f);
			this.revalidate();
		}
	}
	
	
}
