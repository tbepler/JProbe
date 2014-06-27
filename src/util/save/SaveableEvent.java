package util.save;

public class SaveableEvent {
	
	public enum Type{
		CHANGED,
		SAVING,
		SAVED,
		LOADING,
		LOADED,
		IMPORTING,
		IMPORTED,
		FAILED;
	}
	
	public final Type type;
	public final String source;
	
	public SaveableEvent(Type type, String source){
		this.type = type;
		this.source = source;
	}
	
	public SaveableEvent(Type type){
		this(type, null);
	}
	
}
