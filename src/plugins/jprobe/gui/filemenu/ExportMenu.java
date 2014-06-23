package plugins.jprobe.gui.filemenu;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;

public class ExportMenu extends JMenu implements CoreListener{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore m_Core;
	private JFileChooser m_FileChooser;
	private Map<Data, JMenuItem> m_Items;
	
	public ExportMenu(JProbeCore core, JFileChooser exportChooser){
		super("Export");
		m_Core = core;
		m_Core.addCoreListener(this);
		m_FileChooser = exportChooser;
		m_Items = new HashMap<Data, JMenuItem>();
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				checkAllCoreData();
				setEnabled(!m_Items.isEmpty());
			}
			
		});
		
	}
	
	private void checkAllCoreData(){
		for(Data data : m_Core.getDataManager().getAllData()){
			if(m_Core.getDataManager().isWritable(data.getClass())){
				this.addExportItem(data);
			}else{
				this.removeExportItem(data);
			}
		}
	}
	
	private void addExportItem(Data data){
		if(m_Items.containsKey(data)){
			this.remove(m_Items.get(data));
		}
		JMenuItem item = new ExportMenuItem(data, m_Core, m_FileChooser);
		m_Items.put(data, item);
		this.add(item);
		this.revalidate();
		this.setEnabled(!m_Items.isEmpty());
	}
	
	private void clearItems(){
		m_Items.clear();
		this.removeAll();
		this.setEnabled(!m_Items.isEmpty());
		this.revalidate();
	}
	
	private void removeExportItem(Data data){
		if(m_Items.containsKey(data)){
			this.remove(m_Items.get(data));
			m_Items.remove(data);
			this.setEnabled(!m_Items.isEmpty());
			this.revalidate();
		}
	}
	
	public void cleanup(){
		m_Core.removeCoreListener(this);
	}
	
	protected void process(CoreEvent event){
		switch(event.type()){
		case DATA_ADDED:
			Data added = event.getData();
			if(m_Core.getDataManager().isWritable(added.getClass())){
				this.addExportItem(added);
			}
			break;
		case DATA_REMOVED:
			this.removeExportItem(event.getData());
			break;
		case DATA_NAME_CHANGE:
			Data changed = event.getData();
			if(m_Items.containsKey(changed)){
				m_Items.get(changed).setText(m_Core.getDataManager().getDataName(changed));
			}
			break;
		case WORKSPACE_CLEARED:
			this.clearItems();
			break;
		case WORKSPACE_LOADED:
			this.checkAllCoreData();
			break;
		case DATAWRITER_ADDED:
			this.checkAllCoreData();
			break;
		case DATAWRITER_REMOVED:
			this.checkAllCoreData();
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
