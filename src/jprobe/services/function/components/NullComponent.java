package jprobe.services.function.components;

import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import util.gui.ChangeNotifier;

public class NullComponent extends JPanel implements ChangeNotifier{
	private static final long serialVersionUID = 1L;

	@Override
	public void addChangeListener(ChangeListener l) {
		//do nothing
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
		//do nothing
	}
	
}
