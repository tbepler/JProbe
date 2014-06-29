package util.gui;

import javax.swing.event.ChangeListener;

public interface ChangeNotifier {
	
	public void addChangeListener(ChangeListener l);
	public void removeChangeListener(ChangeListener l);
	
}
