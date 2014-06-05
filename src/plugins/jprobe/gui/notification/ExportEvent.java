package plugins.jprobe.gui.notification;

import java.io.File;

public class ExportEvent {
	
	public enum Type{
		EXPORTING,
		EXPORTED,
		FAILED;
	}
	
	public final Type type;
	public final String name;
	public final File file;
	
	public ExportEvent(Type t, String dataName, File f){
		type = t; name = dataName; file = f;
	}
	
}
