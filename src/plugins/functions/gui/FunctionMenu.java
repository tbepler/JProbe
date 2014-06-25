package plugins.functions.gui;

import java.awt.Color;
import java.awt.Frame;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

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
	private Map<String, Collection<Function<?>>> m_Categories = new TreeMap<String, Collection<Function<?>>>();
	private Map<Function<?>, JMenuItem> m_Items = new HashMap<Function<?>, JMenuItem>();;
	private FunctionDialogHandler m_FunctionWindow;
	
	public FunctionMenu(Frame owner, JProbeCore core, Bundle bundle){
		super("Functions");
		m_Core = core;
		m_Bundle = bundle;
		m_FunctionWindow = new FunctionDialogHandler(owner, false);
		for(Function<?> f : core.getFunctionManager().getAllFunctions()){
			this.addFunction(f);
		}
		this.layoutMenuItems();
		//this.add(new ErrorTest(bundle));
		m_Core.getFunctionManager().addListener(this);
		this.setVisible(true);
		this.setEnabled(true);
	}
	
	private void addFunction(Function<?> f){
		String cat = f.getCategory();
		if(m_Categories.containsKey(cat)){
			m_Categories.get(cat).add(f);
		}else{
			Collection<Function<?>> group = new PriorityQueue<Function<?>>(10, new Comparator<Function<?>>(){

				@Override
				public int compare(Function<?> arg0, Function<?> arg1) {
					return arg0.getName().compareTo(arg1.getName());
				}
				
			});
			group.add(f);
			m_Categories.put(cat, group);
		}
		m_Items.put(f, new FunctionMenuItem(m_Core, m_Bundle, f, m_FunctionWindow));
	}
	
	private void removeFunction(Function<?> f){
		String cat = f.getCategory();
		if(m_Categories.containsKey(cat)){
			Collection<Function<?>> group = m_Categories.get(cat);
			group.remove(f);
			if(group.isEmpty()){
				m_Categories.remove(cat);
			}
		}
		m_Items.remove(f);
	}
	
	private void layoutMenuItems(){
		this.removeAll();
		boolean first = true;
		for(String cat : m_Categories.keySet()){
			//this.add(this.newSeparator(cat));
			if(first){
				first = false;
			}else{
				this.addSeparator();
			}
			for(Function<?> f : m_Categories.get(cat)){
				this.add(m_Items.get(f));
			}
		}	
	}
	
	protected JComponent newSeparator(String title){
		JPanel panel = new JPanel();
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLUE));
		border.setTitle(title);
		border.setTitleJustification(TitledBorder.CENTER);
		border.setTitlePosition(TitledBorder.BOTTOM);
		panel.setBorder(border);
		return panel;
	}
	
	void cleanup(){
		m_Core.getFunctionManager().removeListener(this);
	}

	@Override
	public void update(CoreEvent event) {
		if(event.type() == CoreEvent.Type.FUNCTION_ADDED){
			Function<?> f = event.getFunction();
			this.addFunction(f);
			this.layoutMenuItems();
		}
		if(event.type() == CoreEvent.Type.FUNCTION_REMOVED){
			Function<?> f = event.getFunction();
			this.removeFunction(f);
			this.layoutMenuItems();
		}
	}
	
	
}
