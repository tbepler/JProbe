package plugins.genome.gui.dialog;

import java.io.File;

import javax.swing.JPanel;

import util.gui.StateListener;
import util.gui.ValidStateNotifier;

public class GenomeFilePanel extends JPanel implements ValidStateNotifier{
	private static final long serialVersionUID = 1L;
	
	public File getGenomeFile(){
		return null;
	}
	
	@Override
	public void addStateListener(StateListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeStateListener(StateListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isStateValid() {
		// TODO Auto-generated method stub
		return false;
	}

}
