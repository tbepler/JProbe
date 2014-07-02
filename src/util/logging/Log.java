package util.logging;

public interface Log {
	
	public enum Level{
		DEBUG,
		ERROR,
		WARNING,
		INFO
	}
	
	public void write(Level l, String output);
	
}
