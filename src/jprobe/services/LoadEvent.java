package jprobe.services;

import java.io.File;

public class LoadEvent {
	
	public enum Type{
		LOADING,
		LOADED,
		FAILED;
	}
	
	public final Type type;
	public final File file;
	
	public LoadEvent(Type t, File f){
		this.type = t; this.file = f;
	}

}
