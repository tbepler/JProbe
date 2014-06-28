package jprobe.services.function.components;

public interface ChangeNotifier {
	
	public void addChangeListener(ChangeListener l);
	public void removeChangeListener(ChangeListener l);
	
}
