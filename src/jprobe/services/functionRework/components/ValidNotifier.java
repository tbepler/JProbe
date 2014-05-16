package jprobe.services.functionRework.components;

public interface ValidNotifier {
	
	public boolean isStateValid();
	public void addListener(ValidListener l);
	public void removeListener(ValidListener l);
	
}
