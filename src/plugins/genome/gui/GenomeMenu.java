package plugins.genome.gui;

import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import jprobe.services.JProbeCore;
import plugins.genome.gui.dialog.GenomeDialogHandler;
import plugins.genome.gui.dialog.GenomeFilePanel;
import plugins.genome.services.GenomeFunction;
import plugins.genome.services.GenomeService;
import plugins.genome.services.GenomeServiceEvent;
import plugins.genome.services.GenomeServiceEvent.Type;
import plugins.genome.services.GenomeServiceListener;

public class GenomeMenu extends JMenu implements GenomeServiceListener{
	private static final long serialVersionUID = 1L;
	
	private JProbeCore m_Core;
	private GenomeService m_GenomeCore;
	private GenomeFilePanel m_FilePanel;
	private GenomeDialogHandler m_DialogHandler;
	private Map<GenomeFunction, JMenuItem> m_Items;
	
	public GenomeMenu(Frame owner, JProbeCore core, GenomeService genomeCore){
		super("Genome");
		m_Core = core;
		m_GenomeCore = genomeCore;
		m_GenomeCore.addGenomeServiceListener(this);
		m_FilePanel = new GenomeFilePanel();
		m_DialogHandler = new GenomeDialogHandler(owner, false);
		m_Items = new HashMap<GenomeFunction, JMenuItem>();
		for(GenomeFunction f : m_GenomeCore.getAllGenomeFunctions()){
			this.addFunction(f);
		}
	}
	
	private void addFunction(GenomeFunction f){
		if(!m_Items.containsKey(f)){
			JMenuItem menu = new GenomeMenuItem(f, m_FilePanel, m_Core, m_DialogHandler);
			m_Items.put(f, menu);
			this.add(menu);
			this.revalidate();
		}
	}
	
	private void removeFunction(GenomeFunction f){
		if(m_Items.containsKey(f)){
			JMenuItem menu = m_Items.get(f);
			this.remove(menu);
			m_Items.remove(f);
			this.revalidate();
		}
	}
	
	public void cleanup(){
		m_GenomeCore.removeGenomeServiceListener(this);
		for(JMenuItem menu : m_Items.values()){
			this.remove(menu);
		}
		m_Items.clear();
	}

	@Override
	public void update(GenomeServiceEvent event) {
		if(event.getType() == Type.FUNCTION_ADDED){
			this.addFunction(event.getChanged());
		}
		if(event.getType() == Type.FUNCTION_REMOVED){
			this.removeFunction(event.getChanged());
		}
	}
	
}
