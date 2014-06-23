package plugins.jprobe.gui.filemenu;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class ImportMenu extends JMenu implements CoreListener{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore m_Core;
	private JFileChooser m_FileChooser;
	private Map<Class<? extends Data>, JMenuItem> m_Items;
	
	public ImportMenu(JProbeCore core, JFileChooser importChooser){
		super("Import");
		m_Core = core;
		m_Core.addCoreListener(this);
		m_FileChooser = importChooser;
		m_Items = new TreeMap<Class<? extends Data>, JMenuItem>(new Comparator<Class<? extends Data>>(){

			@Override
			public int compare(Class<? extends Data> arg0, Class<? extends Data> arg1) {
				return arg0.getSimpleName().compareTo(arg1.getSimpleName());
			}
			
		});
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				for(Class<? extends Data> readable : m_Core.getDataManager().getReadableDataTypes()){
					addImportItem(readable);
				}
				setEnabled(!m_Items.isEmpty());
			}
			
		});
		
	}
	
	public void cleanup(){
		m_Core.removeCoreListener(this);
	}
	
	private void allocateItems(){
		this.removeAll();
		for(Class<? extends Data> clazz : m_Items.keySet()){
			this.add(m_Items.get(clazz));
		}
	}
	
	private void addImportItem(Class<? extends Data> importClass){
		if(m_Items.containsKey(importClass)){
			this.remove(m_Items.get(importClass));
		}
		JMenuItem item = new ImportMenuItem(importClass, m_Core, m_FileChooser);
		m_Items.put(importClass, item);
		this.allocateItems();
		this.revalidate();
		if(!m_Items.isEmpty()){
			this.setEnabled(true);
		}
	}
	
	private void removeImportItem(Class<? extends Data> importClass){
		if(m_Items.containsKey(importClass)){
			this.remove(m_Items.get(importClass));
			m_Items.remove(importClass);
			if(m_Items.isEmpty()){
				this.setEnabled(false);
			}
		}
	}

	public void process(CoreEvent event){
		switch(event.type()){
		case DATAREADER_ADDED:
			this.addImportItem(event.getDataClass());
			break;
		case DATAREADER_REMOVED:
			this.removeImportItem(event.getDataClass());
			break;
		default:
			//do nothing
		}
	}
	
	@Override
	public void update(final CoreEvent event) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				process(event);
			}
			
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
