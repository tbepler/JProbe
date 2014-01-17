package plugins.functions.gui.utils;

public interface StateNotifier {
	
	public void addStateListener(StateListener l);
	public void removeStateListener(StateListener l);
	
}
