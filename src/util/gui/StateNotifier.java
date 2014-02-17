package util.gui;

public interface StateNotifier {
	
	public void addStateListener(StateListener l);
	public void removeStateListener(StateListener l);
	
}
