package jprobe.services.function.components;

public interface ValidNotifier {
	
	public boolean isStateValid();
	public void addListener(ValidListener l);
	public void removeListener(ValidListener l);
	
}
