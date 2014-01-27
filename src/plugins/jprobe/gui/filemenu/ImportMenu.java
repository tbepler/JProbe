package plugins.jprobe.gui.filemenu;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

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
		m_Items = new HashMap<Class<? extends Data>, JMenuItem>();
		for(Class<? extends Data> readable : m_Core.getDataManager().getReadableDataTypes()){
			this.addImportItem(readable);
		}
		if(m_Items.isEmpty()){
			this.setEnabled(false);
		}
	}
	
	public void cleanup(){
		m_Core.removeCoreListener(this);
	}
	
	private void addImportItem(Class<? extends Data> importClass){
		if(m_Items.containsKey(importClass)){
			this.remove(m_Items.get(importClass));
		}
		JMenuItem item = new ImportMenuItem(importClass, m_Core, m_FileChooser);
		m_Items.put(importClass, item);
		this.add(item);
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

	@Override
	public void update(CoreEvent event) {
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
