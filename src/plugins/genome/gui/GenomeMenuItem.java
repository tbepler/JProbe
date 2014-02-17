package plugins.genome.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.Log;
import plugins.genome.GenomeActivator;
import plugins.genome.gui.dialog.GenomeDialogHandler;
import plugins.genome.gui.dialog.GenomeFilePanel;
import plugins.genome.gui.dialog.GenomePanel;
import plugins.genome.services.GenomeFunction;

public class GenomeMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private GenomePanel m_Panel;
	private GenomeDialogHandler m_Dialog;
	
	public GenomeMenuItem(GenomeFunction function, GenomeFilePanel filePanel, JProbeCore core, GenomeDialogHandler dialogHandler){
		super(function.getName());
		this.setToolTipText(function.getDescription());
		m_Panel = new GenomePanel(function, filePanel, core);
		m_Dialog = dialogHandler;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(Debug.getLevel() == Debug.FULL){
			Log.getInstance().write(GenomeActivator.getBundle(), this.getText()+" clicked");
		}
		m_Dialog.display(m_Panel);
	}
	
}
