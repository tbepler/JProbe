package plugins.jprobe.gui.notification;

import java.io.File;

import jprobe.services.data.Data;

public class ImportEvent {
	
	public enum Type{
		IMPORTING,
		IMPORTED,
		FAILED;
	}
	
	public final Type type;
	public final Class<? extends Data> dataClass;
	public final File file;
	
	public ImportEvent(Type t, Class<? extends Data> clazz, File f){
		type = t; dataClass = clazz; file = f;
	}
	
	
}
