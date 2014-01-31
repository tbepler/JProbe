package plugins.genome.gui;

import javax.swing.JMenu;

import plugins.genome.services.GenomeService;
import plugins.genome.services.GenomeServiceEvent;
import plugins.genome.services.GenomeServiceListener;

public class GenomeMenu extends JMenu implements GenomeServiceListener{
	private static final long serialVersionUID = 1L;
	
	private GenomeService m_Core;
	
	public GenomeMenu(GenomeService core){
		super("Genome");
		m_Core = core;
		m_Core.addGenomeServiceListener(this);
	}
	
	public void cleanup(){
		m_Core.removeGenomeServiceListener(this);
	}

	@Override
	public void update(GenomeServiceEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
