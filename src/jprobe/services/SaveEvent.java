package jprobe.services;

import java.io.File;

public class SaveEvent {
	
	public enum Type{
		SAVING,
		SAVED,
		FAILED;
	}
	
	public final Type type;
	public final File file;
	
	public SaveEvent(Type t, File f){
		this.type = t;
		this.file = f;
	}
	
}
